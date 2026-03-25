/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.service;

import rs.ac.bg.fon.marko.grid.common.dto.response.AlertDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeStateDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.EdgeStateDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.TelemetryDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.EdgeDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.CommandDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeDTO;
import java.util.List;
import rs.ac.bg.fon.marko.grid.common.dto.request.*;
import rs.ac.bg.fon.marko.grid.common.dto.response.*;

/**
 *
 * @author Marko
 */
public interface GridService {
    // TOPOLOGY (staticki model)
    List<NodeDTO> getAllNodes();
    List<EdgeDTO> getAllEdges();
    NodeDTO getNodeById(String id);
    
    // TELEMETRY (podaci iz senzora)
    void receiveTelemetryBatch(TelemetryBatchRequest request);
    List<TelemetryDTO> getLatestTelemetry();
    List<TelemetryDTO> getTelemetryForNode(String nodeId);
    
    // STATE (trenutno stanje)
    List<NodeStateDTO> getAllNodeStates();
    List<EdgeStateDTO> getAllEdgeStates();
    FullStateResponse getFullState();
    
    // COMMANDS (izvrsavanje komadni)
    void executeCommand(CommandRequest request);
    List<CommandDTO> getCommandHistory(String nodeId);
    
    // ALERTS (alarmi)
    void createAlert(AlertRequest request);
    List<AlertDTO> getActiveAlerts();
    void resolveAlert(Long alertId);
}
