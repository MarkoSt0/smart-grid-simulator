/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.simulation.service.clock;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Marko
 */
@Component
@Getter
public class SimulationClock {
    private int currentHour = 0;
    private int currentMinute = 0;
    
    // Svake sekunde ce se pokrenuti funkcija koja dodaje 15min u sistemu
    // za potrebe simulacije je postavljeno ovako, u realnim sistemima bi islo
    // u realnom vremenu, onako kako sistem dobija podatke senzora na terenu
    @Scheduled(fixedRate = 1000)
    public void tick() {
        currentMinute += 15;
        if (currentMinute >= 60) {
            currentMinute = 0;
            currentHour = (currentHour + 1) % 24;
        }
    }
    // Fiktivno vreme koje ce da se unosi u bazu
    public LocalDateTime getSimulationDateTime() {
        return LocalDateTime.now()
                .withHour(currentHour)
                .withMinute(currentMinute)
                .withSecond(0)
                .withNano(0);
    }
}
