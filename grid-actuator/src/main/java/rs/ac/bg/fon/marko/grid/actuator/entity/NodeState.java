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
@Table(name = "node_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//NodeState su promenljivi delovi Node klase, izvuceni u posebnu klasu jer ce se
//cesto cuvati i menjati u bazi!
public class NodeState {
    @Id
    @Column(name = "node_id", length = 50)
    private String nodeId;
    
//    @Column(name = "current_mw", precision = 10, scale = 2)
    @Column(name = "current_mw")
    private Double currentMw;
    
//    @Column(name = "current_soc_percent", precision = 5, scale = 2)
    @Column(name = "current_soc_percent")
    private Double currentSocPercent;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
