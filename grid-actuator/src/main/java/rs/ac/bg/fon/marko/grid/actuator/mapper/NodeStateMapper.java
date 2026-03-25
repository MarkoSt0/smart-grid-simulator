/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeStateDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.NodeState;
/**
 *
 * @author Marko
 */
public class NodeStateMapper {
    public NodeStateDTO toDto(NodeState entity) {
        if (entity == null) return null;
        
        return NodeStateDTO.builder()
                .nodeId(entity.getNodeId())
                .currentMw(entity.getCurrentMw())
                .currentSocPercent(entity.getCurrentSocPercent())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }
    
    public NodeState toEntity(NodeStateDTO dto) {
        if (dto == null) return null;
        
        return NodeState.builder()
                .nodeId(dto.getNodeId())
                .currentMw(dto.getCurrentMw())
                .currentSocPercent(dto.getCurrentSocPercent())
                .lastUpdated(dto.getLastUpdated())
                .build();
    }
}
