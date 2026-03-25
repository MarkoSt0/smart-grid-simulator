/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.marko.grid.common.dto.response.AlertDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.Alert;
/**
 *
 * @author Marko
 */
@Component
public class AlertMapper {
    public AlertDTO toDto(Alert entity) {
        if (entity == null) return null;
        
        return AlertDTO.builder()
                .id(entity.getId())
                .alertType(entity.getAlertType())
                .severity(entity.getSeverity())
                .description(entity.getDescription())
                .resolved(entity.getResolved())
                .createdAt(entity.getCreatedAt())
                .resolvedAt(entity.getResolvedAt())
                .build();
    }
    
    public Alert toEntity(AlertDTO dto) {
        if (dto == null) return null;
        
        return Alert.builder()
                .id(dto.getId())
                .alertType(dto.getAlertType())
                .severity(dto.getSeverity())
                .description(dto.getDescription())
                .resolved(dto.getResolved())
                .createdAt(dto.getCreatedAt())
                .resolvedAt(dto.getResolvedAt())
                .build();
    }
}
