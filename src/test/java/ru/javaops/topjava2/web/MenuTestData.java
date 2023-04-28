package ru.javaops.topjava2.web;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT1;

public class MenuTestData {
    public static final int MENU1_ID = 1;
    public static final int MENU2_ID = 2;
    public static final int MENU3_ID = 3;

    public static final Menu MENU1 = new Menu(MENU1_ID, "Меню дня", RESTAURANT1, null, LocalDate.now());
    public static final Menu MENU2 = new Menu(MENU2_ID, "Меню дня", RESTAURANT1, null, LocalDate.now());
    public static final Menu MENU3 = new Menu(MENU3_ID, "Меню дня", RESTAURANT1, null, LocalDate.now());

    static {
        List<Dish> MENU1_DISHES = new ArrayList<>(List.of(
                new Dish(1, "Голубцы", MENU1, 250),
                new Dish(2, "Омлет", MENU1, 150),
                new Dish(3, "Суп", MENU1, 90)
        ));
        MENU1_DISHES.sort(Comparator.comparing(Dish::getName));
        MENU1.setDishes(MENU1_DISHES);

        List<Dish> MENU2_DISHES = new ArrayList<>(List.of(
                new Dish(4, "Суп", MENU2, 90),
                new Dish(5, "Омлет", MENU2, 150),
                new Dish(6, "Голубцы", MENU2, 250),
                new Dish(7, "Брускеты с паштетом", MENU2, 350)
        ));
        MENU2_DISHES.sort(Comparator.comparing(Dish::getName));
        MENU2.setDishes(MENU2_DISHES);

        List<Dish> MENU3_DISHES = new ArrayList<>(List.of(
                new Dish(8, "Суп", MENU3, 90),
                new Dish(9, "Цыпленок запеченый", MENU3, 410),
                new Dish(10, "Омлет", MENU3, 150),
                new Dish(11, "Брускеты с паштетом", MENU3, 350),
                new Dish(12, "Голубцы", MENU3, 250)
        ));
        MENU3_DISHES.sort(Comparator.comparing(Dish::getName));
        MENU3.setDishes(MENU3_DISHES);
    }
}
