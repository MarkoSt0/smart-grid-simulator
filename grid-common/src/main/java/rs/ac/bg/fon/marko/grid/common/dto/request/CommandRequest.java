/*
    CommandRequest koristi Smart-Orchestrator mikroservis kada zahteva izvrsenje
    neke komande (punjenje, praznjenje baterije...)
*/
package rs.ac.bg.fon.marko.grid.common.dto.request;

import lombok.*;

/**
 *
 * @author Marko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandRequest {
    private String commandType; // CHARGE_BATTERY, DISCHARGE_BATTERY, SHED_LOAD
    private String targetNode;
    private Double value;
    private String issuedBy;
}
