package rs.ac.bg.fon.marko.grid.actuator.entity;

/**
 *
 * @author Marko
 */

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "nodes")
// Lombok anotacija koja u sebi sadrzi 5 anotacija:
// @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor(final)
@Data
@NoArgsConstructor
@AllArgsConstructor
// Builder design pattern
@Builder
public class Node {
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false, length = 20)
    private String type; // SOURCE, CONSUMER, HUB, SUBSTATION, STORAGE
    
    //precision i scale nisu moguci za double, moguca promena u buducnosti
//    @Column(name = "max_capacity_mw", precision = 10, scale = 2)
    @Column(name = "max_capacity_mw")
    private Double maxCapacityMw;
    
    @Column(name = "capacity_mwh")
    private Double capacityMwh; // Samo za STORAGE
    
    @Column(name = "base_load")
    private Double baseLoad; // Samo za CONSUMER
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; // JSON kao String
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
