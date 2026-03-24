/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.dto;
import lombok.*;
import java.time.LocalDateTime;
/**
 *
 * @author Marko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EdgeStateDTO {
    private Long edgeId;
    private Double currentFlowMw;
    private Double utilizationPercent;
    private LocalDateTime lastUpdated;
}
