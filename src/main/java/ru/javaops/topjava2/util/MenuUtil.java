package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.to.CreateMenuTo;
import ru.javaops.topjava2.to.MenuTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuUtil {
    public static Menu createFromTo(CreateMenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getName(), null, menuTo.getDishes(), LocalDate.now());
    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getName(), menu.getRestaurant().getId(), menu.getDishes(), menu.getLastUpdate());
    }

    public static List<MenuTo> createTos(Iterable<Menu> menus) {
        List<MenuTo> menuTos = new ArrayList<>();
        for (Menu menu : menus) {
            menuTos.add(createTo(menu));
        }
        return menuTos;
    }
}
