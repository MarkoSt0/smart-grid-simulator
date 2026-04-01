/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.controller.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.ac.bg.fon.marko.grid.common.dto.request.AlertRequest;
import rs.ac.bg.fon.marko.grid.common.dto.request.CommandRequest;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeStateDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.TelemetryDTO;

/**
 * Feign Client ka Grid-Actuator mikroservisu
 * 
 * 
 * Metode:
 * 1. getLatestTelemetry() - GET /api/telemetry/latest
 * 2. getAllNodeStates() - GET /api/state/nodes
 * 3. executeCommand() - POST /api/commands/execute
 * 4. createAlert() - POST /api/alerts
 * 
 * Grid-Actuator radi na ${services.grid-actuator.url}
 * 
 * @author Marko
 */
@FeignClient(name = "grid-actuator", url = "${services.grid-actuator.url}")
public interface GridActuatorClient {
    
    @GetMapping("/api/telemetry/latest")
    public List<TelemetryDTO> getLatestTelemetry();
    
    @GetMapping("/api/state/nodes")
    public List<NodeStateDTO> getAllNodeStates();
    
    @PostMapping("/api/commands/execute")
    public void executeCommand(@RequestBody CommandRequest request);
    
    @PostMapping("/api/alerts")
    public void createAlert(@RequestBody AlertRequest request);
}
