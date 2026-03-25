package rs.ac.bg.fon.marko.grid.actuator.entity;

/**
 *
 * @author Marko
 */

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "edges", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"from_node", "to_node"})})
// Lombok anotacija koja u sebi sadrzi 5 anotacija:
// @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor(final)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "from_node", nullable = false, length = 50)
    private String fromNode;
    
    @Column(name = "to_node", nullable = false, length = 50)
    private String toNode;
    
//    @Column(name = "limit_mw", nullable = false, precision = 10, scale = 2)
    @Column(name = "limit_mw", nullable = false)
    private Double limitMw;
}
