/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.marko.grid.actuator.dto.NodeDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.Node;

/**
 *
 * @author Marko
 */
@Component
public class NodeMapper {
    public NodeDTO toDto(Node entity) {
        if (entity == null) return null;
        
        return NodeDTO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .maxCapacityMw(entity.getMaxCapacityMw())
                .capacityMwh(entity.getCapacityMwh())
                .baseLoad(entity.getBaseLoad())
                .metadata(entity.getMetadata())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    
    public Node toEntity(NodeDTO dto) {
        if (dto == null) return null;
        
        return Node.builder()
                .id(dto.getId())
                .type(dto.getType())
                .maxCapacityMw(dto.getMaxCapacityMw())
                .capacityMwh(dto.getCapacityMwh())
                .baseLoad(dto.getBaseLoad())
                .metadata(dto.getMetadata())
                .createdAt(dto.getCreatedAt())
                .build();
    }
    
}
