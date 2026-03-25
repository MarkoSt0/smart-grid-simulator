/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.common.dto.request;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import rs.ac.bg.fon.marko.grid.common.dto.TelemetryReading;
/**
 *
 * @author Marko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetryBatchRequest {
    private LocalDateTime timestamp;
    private List<TelemetryReading> readings;
}
