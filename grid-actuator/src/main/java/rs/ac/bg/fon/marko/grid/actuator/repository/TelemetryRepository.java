/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.bg.fon.marko.grid.actuator.entity.Telemetry;

/**
 *
 * @author Marko
 */
public interface TelemetryRepository extends JpaRepository<Telemetry, Long>{
    //Poslednja sacuvana telemetrija za svaki node
    @Query(value = """
        SELECT t.* FROM telemetry t
        WHERE t.id IN (
            SELECT MAX(t2.id) 
            FROM telemetry t2 
            GROUP BY t2.node_id
        )
        ORDER BY t.node_id
    """, nativeQuery = true)
    List<Telemetry> findLatestByNodeId();
    
//    @Query("SELECT MAX(t.timestamp) FROM Telemetry t")
//    LocalDateTime findLatestTimestamp();
//
//    @Query("SELECT t FROM Telemetry t WHERE t.timestamp = :timestamp ORDER BY t.nodeId")
//    List<Telemetry> findByTimestamp(@Param("timestamp") LocalDateTime timestamp);
    
    // Sve telemetrije jednog cvora u vremenskom periodu (grafikoni)
    List<Telemetry> findByNodeIdAndTimestampBetweenOrderByTimestampAsc(
        String nodeId, 
        LocalDateTime start, 
        LocalDateTime end
    );
    
    // Poslednja telemetrija za neki cvor
    Telemetry findFirstByNodeIdOrderByTimestampDesc(String nodeId);
}
