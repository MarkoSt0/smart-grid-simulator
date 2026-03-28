/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.simulation.service.simulation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.marko.grid.common.dto.TelemetryReading;
import rs.ac.bg.fon.marko.grid.common.dto.request.TelemetryBatchRequest;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeDTO;
import rs.ac.bg.fon.marko.simulation.service.client.GridClient;
import rs.ac.bg.fon.marko.simulation.service.clock.SimulationClock;

/**
 * Trenutno u doradi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SimulationService {
    
    private final GridClient gridClient;
    private final SimulationClock clock;
    
    private int tickCounter = 0;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Glavna simulaciona petlja
     * 
     * Svake 1 sekunde (realnog vremena):
     * 1. Generise proizvodnju (SOURCE): wind + hydro
     * 2. Generise potrosnju (CONSUMER): residential zones
     * 3. Salje batch telemetriju ka Grid-Actuator-u
     * 
     * Simulaciono vreme: +15 minuta po tick-u
     */
    @Scheduled(fixedRate = 1000)
    public void runSimulationStep() {
        if (!isGridActuatorReady()) {
            log.warn("Simulacija pauzirana - Grid-Actuator nije dostupan...");
            return; 
        }
        if (tickCounter >= 96) {
            stopSimulation();
            return;
        }
        int hour = clock.getCurrentHour();
        LocalDateTime simulationTime = clock.getSimulationDateTime();
        
        log.debug("Tick #{} - Simulation time: {} (hour {})", 
                tickCounter, simulationTime.toLocalTime(), hour);
        // 1. Ucitavanje topologije mreze (cvorove)
        List<NodeDTO> nodes = gridClient.getAllNodes();
        
        // 2. Kreiranje telemetrijskih podataka
        List<TelemetryReading> tlms = new ArrayList<>();
        
        for (NodeDTO node : nodes) {
            if ("SOURCE".equals(node.getType())) {
                double production = node.getId().startsWith("WIND") 
                    ? calculateWind(node.getMaxCapacityMw(), hour) 
                    : calculateHydro(node.getMaxCapacityMw(), hour);
                
                tlms.add(new TelemetryReading(node.getId(), production));
                
                log.trace("  {} → {:.2f} MW", node.getId(), production);
            } else if ("CONSUMER".equals(node.getType())) {
                double consumption = calculateConsumer(node.getBaseLoad(), hour);
                
                tlms.add(new TelemetryReading(node.getId(), consumption));
                
                log.trace("  {} → {:.2f} MW", node.getId(), consumption);
            }
        }
        try {
            gridClient.sendTelemetryBatch(new TelemetryBatchRequest(simulationTime, tlms));
            log.info("Tick #{}: Sent {} readings for time {}", 
                    tickCounter, tlms.size(), simulationTime.toLocalTime());
        } catch (Exception e) {
            log.error("Failed to send telemetry batch: {}", e.getMessage());
        }
        tickCounter++;
    }

    /**
     * Simulira potrosnju
     * 
     * Karakteristike:
     * - Jutarnji peak (~8h)
     * - Vecernji peak (~20h)
     * - Noćni minimum (~3h)
     * - Randomizacija ±15%
     * 
     * @param baseLoad Bazna potrosnja zone (MW)
     * @param h Sat dana (0-23)
     * @return Negativna vrednost (potrosnja uzima energiju)
     */
    private Double calculateConsumer(double baseLoad, int h) {
        double loadNoise = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
        double profile = 0.2 + 0.5 * Math.exp(-Math.pow(h - 8, 2) / 8.0) 
                             + 0.7 * Math.exp(-Math.pow(h - 20, 2) / 12.0);
        return loadNoise * baseLoad * profile * -1;
    }

    /**
     * Simulira proizvodnju vetrenjace
     * 
     * Karakteristike:
     * - Kosinusna funkcija (vetar varira tokom dana)
     * - Peak oko 15h
     * - Minimum ujutru (oko 6h)
     * - Randomizacija ±2%
     * 
     * @param maxCapacity Maksimalna snaga vetrenjače (MW)
     * @param h Sat dana (0-23)
     * @return Pozitivna vrednost (proizvodnja)
     */
    private Double calculateWind(double maxCapacity, int h) {
        double windNoise = ThreadLocalRandom.current().nextDouble(0.98, 1.02);
        return windNoise * maxCapacity * (0.4 + 0.5 * Math.abs(Math.cos(Math.toRadians(h * 15 - 30))));
        
    }
    
    /**
     * Simulira proizvodnju hidroelektrane
     * 
     * Karakteristike:
     * - Sinusna funkcija (kontrolisan protok)
     * - Stabilnija od vetra (80% bazni load)
     * - Male oscilacije (±10%)
     * 
     * @param maxCapacity Maksimalna snaga hidroelektrane (MW)
     * @param h Sat dana (0-23)
     * @return Pozitivna vrednost (proizvodnja)
     */
    private Double calculateHydro(double maxCapacity, int h) {
        return maxCapacity * (0.8 + 0.1 * Math.sin(Math.toRadians(h * 15)));
    }

    private void stopSimulation() {
        System.out.println("---------------SIMULACIJA ZAVRSENA---------------");
        System.exit(0);
    }
    
    private boolean isGridActuatorReady() {
        try {
            String url = "http://localhost:8081/api/test-health";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            log.info("Provera Actuator-a na {}: Status={}", url, response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().contains("UP");
            }
        } catch (Exception e) {
            log.error("Actuator nedostupan: {}", e.getMessage());
        }
        return false;
    }
}
