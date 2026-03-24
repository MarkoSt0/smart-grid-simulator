/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Marko
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeDTO {
    private String id;
    private String type;
    private Double maxCapacityMw;
    private Double currentMw;
    private Double capacityMwh;
    private Double currentSocPercent;
    private LocalDateTime lastUpdated;
}
