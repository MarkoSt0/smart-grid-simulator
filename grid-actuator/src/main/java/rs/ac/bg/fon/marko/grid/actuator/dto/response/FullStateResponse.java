/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.dto.response;
import lombok.*;
import java.util.List;
import rs.ac.bg.fon.marko.grid.actuator.dto.*;
/**
 *
 * @author Marko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullStateResponse {
    private List<NodeDTO> nodes;
    private List<EdgeDTO> edges;
    private List<NodeStateDTO> nodeStates;
    private List<EdgeStateDTO> edgeStates;
    private List<AlertDTO> activeAlerts;
}
