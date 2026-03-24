/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.marko.grid.actuator.entity.Command;

/**
 *
 * @author Marko
 */
public interface CommandRepository extends JpaRepository<Command, Long>{
    // Pronalazi sve komande za odredjeni cvor u vremenu
    List<Command> findByTargetNodeAndExecutedAtBetweenOrderByExecutedAtDesc(
            String targetNode,
            LocalDateTime start,
            LocalDateTime end
    );
}
