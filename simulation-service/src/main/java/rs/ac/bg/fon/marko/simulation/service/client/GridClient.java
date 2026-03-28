/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.simulation.service.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.ac.bg.fon.marko.grid.common.dto.request.TelemetryBatchRequest;
import rs.ac.bg.fon.marko.grid.common.dto.response.EdgeDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeDTO;

/**
 *
 * @author Marko
 */
@FeignClient(name="grid-actuator", url="${services.grid-actuator.url}")
public interface GridClient {
    
    @GetMapping("/api/topology/nodes")
    List<NodeDTO> getAllNodes();
    
    @GetMapping("/api/topology/edges")
    List<EdgeDTO> getAllEdges();

    @PostMapping("/api/telemetry/batch")
    void sendTelemetryBatch(@RequestBody TelemetryBatchRequest batch);
}
