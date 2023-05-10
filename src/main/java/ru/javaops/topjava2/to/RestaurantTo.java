package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.lang.NonNull;
import ru.javaops.topjava2.model.Menu;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {
    List<Menu> menus;

    Long votesAmount;

    public RestaurantTo(Integer id, String name, @NonNull List<Menu> menus, @NonNull Long votesAmount) {
        super(id, name);
        this.menus = menus;
        this.votesAmount = votesAmount;
    }
}
