package ru.javaops.topjava2.web.menu;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.javaops.topjava2.web.TestData.MENU1;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT1_ID;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingEqualsComparator(Menu.class);
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(MenuTo.class);

    public static final int MENU1_ID = 1;
    public static final int MENU2_ID = 2;
    public static final int MENU3_ID = 3;
    public static final int MENU_NEW_ID = 4;
    public static final int NOT_FOUND = 20;

    public static final Dish NEW_DISH1 = new Dish("NewDish1", 130);
    public static final Dish NEW_DISH2 = new Dish("NewDish2", 230);

    public static MenuTo getNew() {
        return new MenuTo(null, "NewMenu", RESTAURANT1_ID, List.of(NEW_DISH1, NEW_DISH2), LocalDate.now().plusDays(1));
    }

    public static MenuTo getUpdated() {
        List<Dish> newDishes = new ArrayList<>();
        newDishes.add(MENU1.getDishes().get(0));
        newDishes.addAll(List.of(NEW_DISH1, NEW_DISH2));
        return new MenuTo(MENU1_ID, "UpdatedName", RESTAURANT1_ID, newDishes, null);
    }

    public static MenuTo getNewInvalid1() {
        return new MenuTo(null, null, null, null, null);
    }

    public static MenuTo getNewInvalid2() {
        return new MenuTo(null, "A", -1, List.of(new Dish("DishName", 1)), LocalDate.now().minusDays(100));
    }

    public static MenuTo getNewInvalid3() {
        return new MenuTo(null, "NewName", 0, List.of(new Dish("", -1)), LocalDate.now().plusDays(100));
    }

    public static MenuTo getUpdatedInvalid1() {
        return new MenuTo(MENU1_ID, null, RESTAURANT1_ID, null, null);
    }

    public static MenuTo getUpdatedInvalid2() {
        return new MenuTo(MENU1_ID, "A", RESTAURANT1_ID, List.of(new Dish(null, null)), LocalDate.now().minusDays(100));
    }

    public static MenuTo getUpdatedInvalid3() {
        return new MenuTo(MENU1_ID, "NewName", RESTAURANT1_ID, List.of(new Dish("", -1)), LocalDate.now().plusDays(100));
    }
}
