/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.marko.grid.common.dto.response.EdgeStateDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.EdgeState;
/**
 *
 * @author Marko
 */
@Component
public class EdgeStateMapper {
    public EdgeStateDTO toDto(EdgeState entity) {
        if (entity == null) return null;
        
        return EdgeStateDTO.builder()
                .edgeId(entity.getEdgeId())
                .currentFlowMw(entity.getCurrentFlowMw())
                .utilizationPercent(entity.getUtilizationPercent())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }
    
    public EdgeState toEntity(EdgeStateDTO dto) {
        if (dto == null) return null;
        
        return EdgeState.builder()
                .edgeId(dto.getEdgeId())
                .currentFlowMw(dto.getCurrentFlowMw())
                .utilizationPercent(dto.getUtilizationPercent())
                .lastUpdated(dto.getLastUpdated())
                .build();
    }
}
