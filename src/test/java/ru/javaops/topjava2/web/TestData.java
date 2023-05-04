package ru.javaops.topjava2.web;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.javaops.topjava2.web.menu.MenuTestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.admin;
import static ru.javaops.topjava2.web.user.UserTestData.user;
import static ru.javaops.topjava2.web.vote.VoteTestData.*;

public class TestData {
    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "Entity with id=20 not found";

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Сопка", null);
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Сациви", null);
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Густав и Густав", null);

    public static final Vote VOTE1 = new Vote(VOTE1_ID, user, RESTAURANT1, LocalDateTime.of(2023, 4, 4, 9, 0, 0));
    public static final Vote VOTE2 = new Vote(VOTE2_ID, user, RESTAURANT1, LocalDateTime.of(2023, 4, 5, 9, 0, 0));
    public static final Vote VOTE3 = new Vote(VOTE3_ID, user, RESTAURANT1, LocalDateTime.of(2023, 4, 6, 9, 0, 0));
    public static final Vote VOTE4 = new Vote(VOTE4_ID, user, RESTAURANT2, LocalDateTime.of(2023, 4, 7, 9, 0, 0));
    public static final Vote VOTE5 = new Vote(VOTE5_ID, user, RESTAURANT1, LocalDateTime.now());
    public static final Vote VOTE6 = new Vote(VOTE6_ID, admin, RESTAURANT1, LocalDateTime.now());

    public static final Menu MENU1 = new Menu(MENU1_ID, "Меню дня", RESTAURANT1, null, LocalDate.now());
    public static final Menu MENU2 = new Menu(MENU2_ID, "Меню дня", RESTAURANT2, null, LocalDate.now());
    public static final Menu MENU3 = new Menu(MENU3_ID, "Меню дня", RESTAURANT3, null, LocalDate.of(2023, 1, 1));

    static {
        List<Dish> MENU1_DISHES = new ArrayList<>(List.of(
                new Dish("Голубцы", 250),
                new Dish("Омлет", 150),
                new Dish("Суп", 90)
        ));
        MENU1_DISHES.sort(Comparator.comparing(Dish::getName));
        MENU1.setDishes(MENU1_DISHES);

        List<Dish> MENU2_DISHES = new ArrayList<>(List.of(
                new Dish("Суп", 90),
                new Dish("Омлет", 150),
                new Dish("Голубцы", 250),
                new Dish("Брускеты с паштетом", 350)
        ));
        MENU2_DISHES.sort(Comparator.comparing(Dish::getName));
        MENU2.setDishes(MENU2_DISHES);

        List<Dish> MENU3_DISHES = new ArrayList<>(List.of(
                new Dish("Суп", 90),
                new Dish("Цыпленок запеченый", 410),
                new Dish("Омлет", 150),
                new Dish("Брускеты с паштетом", 350),
                new Dish("Голубцы", 250)
        ));
        MENU3_DISHES.sort(Comparator.comparing(Dish::getName));
        MENU3.setDishes(MENU3_DISHES);

        RESTAURANT1.setMenus(List.of(MENU1));
        RESTAURANT2.setMenus(List.of(MENU2));
        RESTAURANT3.setMenus(List.of(MENU3));
    }

}
