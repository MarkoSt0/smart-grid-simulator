/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.marko.grid.actuator.entity.Alert;

/**
 *
 * @author Marko
 */
public interface AlertRepository extends JpaRepository<Alert, Long>{
    // Svi nereseni alarmi (za frontend)
    List<Alert> findByResolvedFalseOrderByCreatedAtDesc();
    
    // Alarmi po severity
    List<Alert> findBySeverityAndResolvedFalseOrderByCreatedAtDesc(String severity);
}
