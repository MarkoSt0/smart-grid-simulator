/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.controller.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.marko.grid.common.dto.request.AlertRequest;
import rs.ac.bg.fon.marko.grid.common.dto.request.CommandRequest;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeStateDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.TelemetryDTO;
import rs.ac.bg.fon.marko.grid.common.enums.AlertType;
import rs.ac.bg.fon.marko.grid.common.enums.CommandType;
import rs.ac.bg.fon.marko.grid.common.enums.Severity;
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
        
        if (Math.abs(netBalance) <= imbalanceThreshold) {
            log.debug("Grid balanced, no action needed");
            return;
        }
        
        CommandType commandType = netBalance > 0 ? CommandType.CHARGE_BATTERY : CommandType.DISCHARGE_BATTERY;
        Map<String, NodeStateDTO> batteries = getBatteryStates();
        NodeStateDTO availableBattery = batteries.values().stream()
                .filter(battery -> canUseBattery(battery, commandType))
                .findFirst()
                .orElse(null);
        
        if (availableBattery == null) {
            log.error("CRITICAL: No batteries available for command: {}", commandType);
            createAlert(
                AlertType.IMBALANCE.toString(), 
                Severity.CRITICAL.toString(), 
                "Grid imbalance of " + netBalance + " MW cannot be resolved. All batteries full/empty!"
            );
            return;  //Sta da radim??? upitno za sad
        }
        
        checkBatteryCritical(availableBattery.getNodeId(), availableBattery.getCurrentSocPercent());
        executeBatteryCommand(availableBattery, netBalance, commandType);
        
        
        // TODO: BONUS - Pozovi SNA svaki N-ti tick (opciono za sada)
        // if (tickCounter % snaAnalysisFrequency == 0) {
        //     log.info("Triggering SNA analysis...");
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
     * @param nodeId Baterija na koju se odnosi
     * @param netBalance Trenutni balans mreze (MW)
     */
    private void executeBatteryCommand(NodeStateDTO battery, double netBalance, CommandType commandType) {
         double power = Math.min(Math.abs(netBalance), maxBatteryPower);
         
         CommandRequest cmd = CommandRequest.builder()
             .commandType(commandType.toString())
             .targetNode(battery.getNodeId())
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
    
    /**
     * Ucitava trenutno stanje svih baterija iz Grid-Actuator-a
     * 
     * Poziv preko Feign klijenta ka /api/state/nodes
     * 
     * @Return Mapa: nodeId: NodeStateDTO
     * Primer povratne vrednosti funkcije:
     * {"BAT_MIN: NodeStateDTO(currentSoC=50%), BAT_EDGE: ..."}
     */
    private Map<String, NodeStateDTO> getBatteryStates(){
        return gridClient.getAllNodeStates().stream()
                .filter(nodeSt -> nodeSt.getCurrentSocPercent() != null)
                .collect(Collectors.toMap(
                        NodeStateDTO::getNodeId,
                        nodeSt -> nodeSt));
    }
    
    /**
     * Provera da li baterija moze da primi/da energiju
     * 
     * Validacije:
     * - Za CHARGE: SoC < 95% (5% je sigurnosna margina za punjenje)
     * - Za DISCHARGE: SoC > 10% (10 % je sigurnosna margina za otpustanje energije)
     * @param battery Stanje baterije
     * @param commandType Tip komande - "CHARGE_BATTERY" ili "DISCHARGE_BATTERY"
     * @return true ako baterija moze da se koristi, ukoliko ne moze false
     */
    private boolean canUseBattery(NodeStateDTO battery, CommandType commandType) {
        if(battery == null || battery.getCurrentSocPercent() == null) return false;
        return switch (commandType) {
            case CHARGE_BATTERY ->
                battery.getCurrentSocPercent() < 95;
            case DISCHARGE_BATTERY ->
                battery.getCurrentSocPercent() > 10;
            default -> false;
        };
    }
    
    /**
     * Proverava i kreira alarme za kriticne SoC vrednosti
     * 
     * Alarmi:
     * - BATTERY_CRITICAL i CRITICAL ako je SoC < 10%
     * - BATTERY_CRITICAL i WARNING ako je SoC < 20%
     * - BATTERY_CRITICAL i WARNING ako je SoC > 95%
     * @param nodeId - Id baterije
     * @param soc - Trenutni SoC (State of Charge (%))
     */
    private void checkBatteryCritical(String nodeId, double soc) {
        if (soc < 10.0) {
            createAlert(AlertType.BATTERY_CRITICAL.toString(), Severity.CRITICAL.toString(),
                    nodeId + " is critically low: " + soc + "%");
        } else if (soc < 20.0) {
            createAlert(AlertType.BATTERY_CRITICAL.toString(), Severity.WARNING.toString(),
                    nodeId + " is running low: " + soc + "%");
        } else if (soc > 95.0) {
            createAlert(AlertType.BATTERY_CRITICAL.toString(), Severity.WARNING.toString(),
                    nodeId + " is almost full: " + soc + "%");
        }
    }
}
