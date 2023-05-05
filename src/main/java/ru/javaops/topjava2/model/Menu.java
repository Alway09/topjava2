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
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "actual_date", "restaurant_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class Menu extends NamedEntity implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Restaurant restaurant;

    @ElementCollection(targetClass = Dish.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "dish", joinColumns = @JoinColumn(name = "menu_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "name"}, name = "uk_menu_id_name")})
    @OrderBy("name ASC")
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Dish> dishes;

    @Column(name = "actual_date", nullable = false, columnDefinition = "date default now()")
    private LocalDate actualDate;

    public Menu(Integer id, String name, Restaurant restaurant, List<Dish> dishes, LocalDate actualDate) {
        super(id, name);
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.actualDate = actualDate;
    }

    public Menu(Menu menu) {
        super(menu.getId(), menu.getName());
        this.restaurant = menu.getRestaurant();
        this.dishes = menu.getDishes();
        this.actualDate = menu.getActualDate();
    }
}
