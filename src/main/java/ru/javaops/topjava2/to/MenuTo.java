package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javaops.topjava2.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTo extends NamedTo {
    Integer restaurantId;

    List<Dish> dishes;

    LocalDate creationDate;

    public MenuTo(Integer id, String name, Integer restaurantId, List<Dish> dishes, LocalDate creationDate) {
        super(id, name);
        this.restaurantId = restaurantId;
        this.dishes = dishes;
        this.creationDate = creationDate;
    }
}
