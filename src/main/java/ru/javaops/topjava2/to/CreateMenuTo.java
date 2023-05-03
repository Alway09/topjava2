package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javaops.topjava2.model.Dish;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateMenuTo extends NamedTo {
    Integer restaurantId;

    List<Dish> dishes;

    public CreateMenuTo(Integer id, String name, Integer restaurantId, List<Dish> dishes) {
        super(id, name);
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }
}
