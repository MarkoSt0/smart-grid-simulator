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
@Table(name = "alerts", indexes = {
    @Index(name = "idx_alerts_unresolved", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Alert cuva upozorenja koja ce slati SNA mikroservis, ukoliko detektuje neki problem
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // EDGE_OVERLOAD, BATTERY_CRITICAL, IMBALANCE
    
    @Column(name = "severity", nullable = false, length = 20)
    private String severity; // WARNING, CRITICAL
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "resolved")
    private Boolean resolved = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
