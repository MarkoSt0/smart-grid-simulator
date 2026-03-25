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
public class TelemetryDTO {
    private Long id;
    private String nodeId;
    private LocalDateTime timestamp;
    private Double reportedMw;
    private String metadata;
}
