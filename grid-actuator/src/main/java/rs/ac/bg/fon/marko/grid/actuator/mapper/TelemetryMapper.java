/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;
import rs.ac.bg.fon.marko.grid.common.dto.response.TelemetryDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.Telemetry;
/**
 *
 * @author Marko
 */
public class TelemetryMapper {
    public TelemetryDTO toDto(Telemetry entity) {
        if (entity == null) return null;
        
        return TelemetryDTO.builder()
                .id(entity.getId())
                .nodeId(entity.getNodeId())
                .timestamp(entity.getTimestamp())
                .reportedMw(entity.getReportedMw())
                .metadata(entity.getMetadata())
                .build();
    }
    
    public Telemetry toEntity(TelemetryDTO dto) {
        if (dto == null) return null;
        
        return Telemetry.builder()
                .id(dto.getId())
                .nodeId(dto.getNodeId())
                .timestamp(dto.getTimestamp())
                .reportedMw(dto.getReportedMw())
                .metadata(dto.getMetadata())
                .build();
    }
}
