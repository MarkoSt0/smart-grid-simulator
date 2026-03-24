/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.marko.grid.actuator.dto.EdgeDTO;
import rs.ac.bg.fon.marko.grid.actuator.dto.NodeDTO;
import rs.ac.bg.fon.marko.grid.actuator.service.GridService;

/**
 *
 * @author Marko
 */

@RestController
@RequestMapping("/api/grid")
@RequiredArgsConstructor
public class GridController {
    
    private final GridService gridService;
    
    @GetMapping("/nodes")
    public List<NodeDTO> getAllNodes() {
        return gridService.getAllNodes();
    }

    @GetMapping("/edges")
    public List<EdgeDTO> getAllEdges() {
        return gridService.getAllEdges();
    }
    
    @PutMapping("/nodes/{id}")
    public ResponseEntity<Void> updateNode(@PathVariable String id, @RequestBody NodeDTO nodeDto) {
//        gridService.updateNode(id, nodeDto);
//        return ResponseEntity.ok().build();
        return null;
    }
}
