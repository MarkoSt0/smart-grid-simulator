/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.mapper;
import rs.ac.bg.fon.marko.grid.actuator.dto.CommandDTO;
import rs.ac.bg.fon.marko.grid.actuator.entity.Command;
/**
 *
 * @author Marko
 */
public class CommandMapper {
    public CommandDTO toDto(Command entity) {
        if (entity == null) return null;
        
        return CommandDTO.builder()
                .id(entity.getId())
                .commandType(entity.getCommandType())
                .targetNode(entity.getTargetNode())
                .value(entity.getValue())
                .issuedBy(entity.getIssuedBy())
                .executedAt(entity.getExecutedAt())
                .metadata(entity.getMetadata())
                .build();
    }
    
    public Command toEntity(CommandDTO dto) {
        if (dto == null) return null;
        
        return Command.builder()
                .id(dto.getId())
                .commandType(dto.getCommandType())
                .targetNode(dto.getTargetNode())
                .value(dto.getValue())
                .issuedBy(dto.getIssuedBy())
                .executedAt(dto.getExecutedAt())
                .metadata(dto.getMetadata())
                .build();
    }
}
