/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.marko.grid.actuator.dto.EdgeDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.Edge;

/**
 *
 * @author Marko
 */
@Component
public class EdgeMapper {
    public EdgeDTO toDto(Edge entity) {
        if (entity == null) return null;
        
        return EdgeDTO.builder()
                .id(entity.getId())
                .fromNode(entity.getFromNode())
                .toNode(entity.getToNode())
                .limitMw(entity.getLimitMw())
                .build();
    }

    public Edge toEntity(EdgeDTO dto) {
        if (dto == null) return null;
        
        return Edge.builder()
                .id(dto.getId())
                .fromNode(dto.getFromNode())
                .toNode(dto.getToNode())
                .limitMw(dto.getLimitMw())
                .build();
    }
}
