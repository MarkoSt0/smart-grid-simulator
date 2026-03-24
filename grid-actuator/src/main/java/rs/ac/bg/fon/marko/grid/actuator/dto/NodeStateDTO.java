/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.dto;

import java.time.LocalDateTime;
import lombok.*;

/**
 *
 * @author Marko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeStateDTO {
    private String nodeId;
    private Double currentMw;
    private Double currentSocPercent;
    private LocalDateTime lastUpdated;
}
