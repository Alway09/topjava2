package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.to.MenuTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

public class MenuUtil {
    public static Menu createFromTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getName(), null, menuTo.getDishes(), menuTo.getCreationDate());

    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getName(), menu.getRestaurant().getId(), menu.getDishes(), menu.getCreationDate());
    }

    public static List<MenuTo> createTos(Iterable<Menu> menus) {
        List<MenuTo> menuTos = new ArrayList<>();
        for (Menu menu : menus) {
            menuTos.add(createTo(menu));
        }
        menuTos.sort(Comparator.comparing(MenuTo::getCreationDate).reversed()
                .thenComparing(MenuTo::getName));
        return menuTos;
    }
}
