/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.service;

import java.util.List;
import rs.ac.bg.fon.marko.grid.actuator.dto.request.*;
import rs.ac.bg.fon.marko.grid.actuator.dto.response.*;
import rs.ac.bg.fon.marko.grid.actuator.dto.*;

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
