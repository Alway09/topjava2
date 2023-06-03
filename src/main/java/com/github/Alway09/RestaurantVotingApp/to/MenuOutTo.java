package com.github.Alway09.RestaurantVotingApp.to;

import com.github.Alway09.RestaurantVotingApp.model.Dish;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuOutTo extends NamedTo {
    List<Dish> dishes;

    public MenuOutTo(Integer id, String name, List<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }
}
