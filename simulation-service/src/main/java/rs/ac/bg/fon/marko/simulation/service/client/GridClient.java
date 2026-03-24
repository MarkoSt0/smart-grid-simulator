/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.ac.bg.fon.marko.simulation.service.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.ac.bg.fon.marko.grid.common.dto.EdgeDTO;
import rs.ac.bg.fon.marko.grid.common.dto.NodeDTO;

/**
 *
 * @author Marko
 */
@FeignClient(name="grid-actuator", url="${services.grid-actuator.url}")
public interface GridClient {
    
    @GetMapping("/api/grid/nodes")
    List<NodeDTO> getAllNodes();
    
    @GetMapping("/api/grid/edges")
    List<EdgeDTO> getAllEdges();

    @PutMapping("/api/grid/nodes/{id}")
    void updateNode(@PathVariable("id") String id, @RequestBody NodeDTO node);
}
