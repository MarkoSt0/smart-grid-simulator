/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.marko.grid.actuator.entity.Edge;

/**
 *
 * @author Marko
 */
public interface EdgeRepository extends JpaRepository<Edge, Integer>{
    
}
