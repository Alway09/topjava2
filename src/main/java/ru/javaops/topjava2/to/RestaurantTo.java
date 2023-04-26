package ru.javaops.topjava2.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javaops.topjava2.model.Menu;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {
    @NotBlank
    Menu menu;

    @NotNull
    Integer votesAmount;

    @NotNull
    Boolean needToUpdate;

    public RestaurantTo(Integer id, String name, Menu menu, Integer votesAmount, boolean needToUpdate) {
        super(id, name);
        this.menu = menu;
        this.votesAmount = votesAmount;
        this.needToUpdate = needToUpdate;
    }
}
