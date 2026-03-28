/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package rs.ac.bg.fon.marko.grid.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Grid Controller
 * 
 * Odgovornosti:
 * - Cita telemetriju sa senzora (preko Grid-Actuator-a)
 * - Racuna balans mreze (proizvodnja - potrosnja)
 * - Donosi odluke o punjenju/praznjenju baterija
 * - Poziva SNA servis za analizu
 * - Salje komande ka Grid-Actuator-u
 * 
 * @author Marko
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class GridController {
    public static void main(String[] args) {
        SpringApplication.run(GridController.class, args);
    }
}
