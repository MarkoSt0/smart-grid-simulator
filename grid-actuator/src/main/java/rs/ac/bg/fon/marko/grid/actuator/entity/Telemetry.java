/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 *
 * @author Marko
 */

@Entity
@Table(name = "telemetry", indexes = {
    @Index(name = "idx_telemetry_time", columnList = "timestamp"),
    @Index(name = "idx_telemetry_node", columnList = "node_id,timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "node_id", nullable = false, length = 50)
    private String nodeId;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "reported_mw", nullable = false, precision = 10, scale = 2)
    private Double reportedMw;
    
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
