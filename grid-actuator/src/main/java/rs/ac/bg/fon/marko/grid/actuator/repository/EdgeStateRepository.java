/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.bg.fon.marko.grid.actuator.entity.EdgeState;

/**
 *
 * @author Marko
 */
public interface EdgeStateRepository extends JpaRepository<EdgeState, Long>{
    // Za potrebe pronalazenja preopterecenih kablova *utilization > threshold
    @Query("SELECT es FROM EdgeState es WHERE es.utilizationPercent > :threshold")
    List<EdgeState> findOverloadedEdges(Double threshold);
}
