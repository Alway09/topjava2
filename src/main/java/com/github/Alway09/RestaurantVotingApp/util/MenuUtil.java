package com.github.Alway09.RestaurantVotingApp.util;

import com.github.Alway09.RestaurantVotingApp.model.Menu;
import com.github.Alway09.RestaurantVotingApp.to.MenuTo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuUtil {
    public static final String MENUS_CACHE_NAME = "menus";
    public static Menu createFromTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getName(), null, menuTo.getDishes(), menuTo.getActualDate());

    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getName(), menu.getRestaurant().getId(), menu.getDishes(), menu.getActualDate());
    }

    public static List<MenuTo> createTos(Iterable<Menu> menus) {
        List<MenuTo> menuTos = new ArrayList<>();
        for (Menu menu : menus) {
            menuTos.add(createTo(menu));
        }
        menuTos.sort(Comparator.comparing(MenuTo::getActualDate).reversed()
                .thenComparing(MenuTo::getName));
        return menuTos;
    }
}
