/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
/**
 *
 * @author Marko
 */
@Entity
@Table(name = "commands", indexes = {
    @Index(name = "idx_commands_time", columnList = "executed_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Klasa Command sluzi za pamcenje komandi u bazi (koje su izvrsene u sistemu)
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "command_type", nullable = false, length = 50)
    private String commandType; // CHARGE_BATTERY, DISCHARGE_BATTERY
    
    @Column(name = "target_node", length = 50)
    private String targetNode;
    
//    @Column(name = "value", precision = 10, scale = 2)
    @Column(name = "value")
    private Double value;
    
    @Column(name = "issued_by", length = 50)
    private String issuedBy; // orkestrator, manual
    
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @PrePersist
    protected void onCreate() {
        if (executedAt == null) {
            executedAt = LocalDateTime.now();
        }
    }
}
