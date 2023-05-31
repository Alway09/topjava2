package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.topjava2.HasId;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "actual_date", "name"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class Menu extends NamedEntity implements HasId {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Restaurant restaurant;

    @ElementCollection(targetClass = Dish.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "dish", joinColumns = @JoinColumn(name = "menu_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "name"}, name = "uk_menu_id_name")})
    @OrderBy("name ASC")
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private List<Dish> dishes;

    @Column(name = "actual_date", nullable = false, columnDefinition = "date default now()")
    @NotNull
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
