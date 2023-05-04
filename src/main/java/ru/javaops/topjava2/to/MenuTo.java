package ru.javaops.topjava2.to;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaops.topjava2.model.Dish;

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


    LocalDate creationDate;

    public MenuTo(Integer id, String name, Integer restaurantId, List<Dish> dishes, LocalDate creationDate) {
        super(id, name);
        this.restaurantId = restaurantId;
        this.dishes = dishes;
        this.creationDate = creationDate;
    }
}
