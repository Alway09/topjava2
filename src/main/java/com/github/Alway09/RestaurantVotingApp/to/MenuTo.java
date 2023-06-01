package com.github.Alway09.RestaurantVotingApp.to;

import com.github.Alway09.RestaurantVotingApp.model.Dish;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTo extends NamedTo {
    @NotNull
    @Min(1)
    Integer restaurantId;

    @NotNull
    List<@NotNull @Valid Dish> dishes;

    LocalDate actualDate;

    public MenuTo(Integer id, String name, Integer restaurantId, List<Dish> dishes, LocalDate actualDate) {
        super(id, name);
        this.restaurantId = restaurantId;
        this.dishes = dishes;
        this.actualDate = actualDate;
    }
}
