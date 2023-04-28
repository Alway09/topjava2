package ru.javaops.topjava2.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javaops.topjava2.model.Menu;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {
    @NotBlank
    List<Menu> menus;

    @NotNull
    Long votesAmount;

    public RestaurantTo(Integer id, String name, List<Menu> menus, Long votesAmount) {
        super(id, name);
        this.menus = menus;
        this.votesAmount = votesAmount;
    }
}
