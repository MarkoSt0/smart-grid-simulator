/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package rs.ac.bg.fon.marko.grid.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Marko
 */
@SpringBootApplication
public class GridActuator {
    public static void main(String[] args) {
        SpringApplication.run(GridActuator.class, args);
        System.out.println("--- Grid-Actuator Mikroservis je uspesno pokrenut ---");
    }
}
