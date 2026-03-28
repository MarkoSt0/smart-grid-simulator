/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.controller.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.marko.grid.common.dto.request.AlertRequest;
import rs.ac.bg.fon.marko.grid.common.dto.request.CommandRequest;
import rs.ac.bg.fon.marko.grid.common.dto.response.TelemetryDTO;
import rs.ac.bg.fon.marko.grid.common.enums.CommandType;
import rs.ac.bg.fon.marko.grid.controller.client.GridActuatorClient;

/**
 * Servis za balansiranje elektro distributivne mreze
 * 
 * Glavna petlja:
 * 1. Ucitava telemetriju (sta senzori kazu)
 * 2. Izracunava net balance (proizvodnja - potrosnja)
 * 3. Proveri SoC baterija (State of Charge)
 * 4. Donesi odluku (charge/discharge/nista)
 * 5. Posalji komandu ka Grid-Actuator-u
 * 
 * @author Marko
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BalancingService {
    
    private final GridActuatorClient gridClient;
    
    @Value("${controller.imbalance-threshold}")
    private double imbalanceThreshold;
    
    @Value("${controller.max-battery-power}")
    private double maxBatteryPower;
    
    private int tickCounter = 0;
    
    /**
     * Glavna petlja balansiranja mreze - poziva se svake sekunde
     * 
     */
    @Scheduled(fixedRateString = "${orchestrator.balance-interval}")
    public void balanceGrid() {
        tickCounter++;
        
        log.info("Tick #{}: Starting grid balancing cycle", tickCounter);
        
        List<TelemetryDTO> telemetry = gridClient.getLatestTelemetry();
        
        double netBalance = calculateNetBalance(telemetry);
        log.info("Net balance: {} MW", netBalance);
        
        if(Math.abs(netBalance) > imbalanceThreshold){
            executeBatteryCommand(netBalance);
        }else{
            log.debug("Grid balanced, no action needed");
        }
        
        // TODO: BONUS - Pozovi SNA svaki N-ti tick (opciono za sada)
        // if (tickCounter % snaAnalysisFrequency == 0) {
        //     log.info("🔍 Triggering SNA analysis...");
        //     // Ovde će kasnije biti poziv ka Topology-SNA servisu
        // }
        
    }
    
    /**
     * Raruna ukupan balans mreze
     * 
     * Formula: netBalance = proizvodnja - potrosnja
     * 
     * Pozitivna vrednost = visak energije (treba puniti bateriju)
     * Negativna vrednost = manjak energije (treba prazniti bateriju)
     * 
     * @param telemetry Lista senzorskih očitavanja
     * @return Net balance (MW)
     */
    private double calculateNetBalance(List<TelemetryDTO> telemetry) {
        return telemetry.stream()
                .mapToDouble(TelemetryDTO::getReportedMw)
                .sum();
    }
    
    /**
     * Izvrsava komandu punjenja/praznjenja baterije
     * 
     * Logika:
     * - Ako netBalance > 0 (visak): CHARGE_BATTERY
     * - Ako netBalance < 0 (manjak): DISCHARGE_BATTERY
     * 
     * @param netBalance Trenutni balans mreze (MW)
     */
    private void executeBatteryCommand(double netBalance) {
        
         String commandType = netBalance > 0 ? 
                 CommandType.CHARGE_BATTERY.toString() : 
                 CommandType.DISCHARGE_BATTERY.toString();
         double power = Math.min(Math.abs(netBalance), maxBatteryPower);
         
         CommandRequest cmd = CommandRequest.builder()
             .commandType(commandType)
             .targetNode("BAT_MAIN")
             .value(power)
             .issuedBy("grid-controller")
             .build();
         
         gridClient.executeCommand(cmd);
         log.info("Sent command: {} with {} MW", commandType, power);
        
    }
    
    /**
     * Kreira alarm kada se detektuje problem
     * 
     * Poziva se kada:
     * - Baterija je kriticno niska (< 10%)
     * - Baterija je skoro puna (> 95%)
     * - Veliki disbalans (> 20 MW)
     * 
     * @param alertType Tip alarma (BATTERY_CRITICAL, IMBALANCE, itd.)
     * @param severity WARNING ili CRITICAL
     * @param description Opis problema
     */
    private void createAlert(String alertType, String severity, String description) {
         AlertRequest alert = AlertRequest.builder()
             .alertType(alertType)
             .severity(severity)
             .description(description)
             .build();
         
         gridClient.createAlert(alert);
         log.warn("Alert created: {} - {}", alertType, description);
    }
}
