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
@Table(name = "edge_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//EngeState su promenljivi delovi Edge klase, izvuceni u posebnu klasu jer ce se
//cesto cuvati i menjati u bazi!
public class EdgeState {
    @Id
    @Column(name = "edge_id")
    private Long edgeId;
    
//    @Column(name = "current_flow_mw", precision = 10, scale = 2)
    @Column(name = "current_flow_mw")
    private Double currentFlowMw;
    
//    @Column(name = "utilization_percent", precision = 5, scale = 2)
    @Column(name = "utilization_percent")
    private Double utilizationPercent;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
