/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.simulation.service.simulation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.marko.grid.common.dto.NodeDTO;
import rs.ac.bg.fon.marko.simulation.service.client.GridClient;
import rs.ac.bg.fon.marko.simulation.service.clock.SimulationClock;

/**
 *
 * @author Marko
 */
@Service
@RequiredArgsConstructor
public class SimulationService {
    
    private final GridClient gridClient;
    private final SimulationClock clock;
    
    private int tickCounter = 0;
    
    // Svake 2 sekunde azuriramo bazu u Actuatoru
    // Imamo 3 funkcije koje daju vrednosti u odnosu na sat
    // Funkcija potrosaca simulira povecanu potrosnju ujutru i uvece
    // Funkcija vetra koristi kosinusnu funkciju
    // Funkcija vode (hidroelektrana) koristi sinusnu funkciju sa malim oscilacijama
    @Scheduled(fixedRate = 2000)
    public void runSimulationStep() {
        int hour = clock.getCurrentHour();
        if (tickCounter >= 96) {
            stopSimulation();
        }
        List<NodeDTO> nodes = gridClient.getAllNodes();

        // 1. Osvezavanje svih izvora i potrosaca
        for (NodeDTO node : nodes) {
            if (node.getType().equals("SOURCE")) {
                node.setCurrentMw(node.getId().startsWith("WIND") ? 
                    calculateWind(node.getMaxCapacityMw(), hour) : 
                    calculateHydro(node.getMaxCapacityMw(), hour));
            } else if (node.getType().equals("CONSUMER")) {
                node.setCurrentMw(calculateConsumer(node.getMaxCapacityMw(), hour));
            }
        }

        // 2. Izracunavanje balansa
        double netBalance = calculateNetBalance(nodes);

        // 3. Baterije se podesavaju tako da kontrolisu balans
        for (NodeDTO node : nodes) {
            if (node.getType().equals("STORAGE")) {
                updateBatteryState(node, netBalance);
                // Smanjujemo netBalance za onoliko koliko je baterija preuzela/dala
                netBalance -= node.getCurrentMw(); 
            }
        }
        // 4. Pošalji nazad u Actuator
        for (NodeDTO node : nodes) {
            gridClient.updateNode(node.getId(), node);
        }
        tickCounter++;
        System.out.println(String.format("[%02d:00] Balans: %.2f MW | Baterija SoC: %.2f%%", 
    hour, netBalance, nodes.stream()
        .filter(n -> n.getType().equals("STORAGE"))
        .findFirst().map(NodeDTO::getCurrentSocPercent).orElse(0.0)));
    }

    private double calculateConsumer(double max, int h) {
        double profile = 0.2 + 0.5 * Math.exp(-Math.pow(h - 8, 2) / 8.0) 
                             + 0.7 * Math.exp(-Math.pow(h - 20, 2) / 12.0);
        return max * profile * -1; // Potrosnja je uvek negativna
    }

    private double calculateWind(double max, int h) {
        return max * (0.4 + 0.5 * Math.abs(Math.cos(Math.toRadians(h * 15 - 30))));
    }
    
    private double calculateHydro(double max, int h) {
        return max * (0.8 + 0.1 * Math.sin(Math.toRadians(h * 15)));
    }
    
    //Za baterije je situacija specificna, potrebno je da prvo izracunamo suficit ili deficit
    private double calculateNetBalance(List<NodeDTO> nodes) {
        // Baterije se ne racunaju u primarni balans!
        return nodes.stream()
                .filter(n -> !n.getType().equals("STORAGE"))
                .mapToDouble(NodeDTO::getCurrentMw)
                .sum();
    }
    
    // Izracunavamo bateriju
    private void updateBatteryState(NodeDTO battery, double netBalance) {
        // Koliko maksimalno moze da drzi energije, koliko sadrzi i koji procenat
        double maxPower = battery.getMaxCapacityMw();
        double energyCapacity = battery.getCapacityMwh();
        double currentSoc = battery.getCurrentSocPercent();

        double chargePower = 0;

        if (netBalance > 0) { 
            // Ako je visak napuni je, ali ne brze od maksimuma!
            chargePower = Math.min(netBalance, maxPower);
            if (currentSoc >= 100) chargePower = 0; // Puna je
        } else { 
            // Ako je manjak u mrezi, praznice se ali ne brze od maksimalne snage praznjenja
            chargePower = Math.max(netBalance, -maxPower);
            if (currentSoc <= 0) chargePower = 0; // Prazna je
        }

        // Izracunaj novi SoC (State of Charge)
        // Formula: Delta_Energy = Snaga * Vreme(0.25h)
        double deltaEnergy = chargePower * 0.25; 
        double newSoc = currentSoc + (deltaEnergy / energyCapacity) * 100;

        // Ogranicavanje SoC na skali od 0 do 100
        battery.setCurrentSocPercent(Math.max(0, Math.min(100, newSoc)));
        battery.setCurrentMw(chargePower); 
    }

    private void stopSimulation() {
        System.out.println("========================================");
        System.out.println("SIMULACIJA ZAVRŠENA: Jedan dan je prošao.");
        System.out.println("Svi podaci su sačuvani u bazi.");
        System.out.println("========================================");
        System.exit(0);
    }
}
