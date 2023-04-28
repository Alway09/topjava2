package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.topjava2.HasId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "restaurant_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class Menu extends NamedEntity implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Restaurant restaurant;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    @OrderBy("name ASC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Dish> dishes;

    @Column(name = "last_update", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDate lastUpdate;

    public Menu(Integer id, String name, Restaurant restaurant, List<Dish> dishes, LocalDate lastUpdate) {
        super(id, name);
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.lastUpdate = lastUpdate;
    }
}
