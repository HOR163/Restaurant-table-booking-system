package ee.hor.tablebooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"table\"")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @NotNull
    @Column(name = "seats_amount", nullable = false)
    private Short seatsAmount;

    @OneToMany(mappedBy = "table")
    private Set<BookingEntity> bookings = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "table_attribute", joinColumns = {@JoinColumn(name = "table_id")}, inverseJoinColumns = {@JoinColumn(name = "attribute_id")})
    private Set<AttributeEntity> attributes = new LinkedHashSet<>();

    @NotNull
    @Column(name="table_number", nullable=false)
    private Short tableNumber;

}