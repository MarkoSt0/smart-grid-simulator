/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.common.dto.response;
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
public class AlertDTO {
    private Long id;
    private String alertType;     // EDGE_OVERLOAD, BATTERY_CRITICAL, IMBALANCE
    private String severity;      // WARNING, CRITICAL
    private String description;
    private Boolean resolved;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
