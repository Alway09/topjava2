package ru.javaops.topjava2.web.restaurant;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.web.MenuTestData.*;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER_EXCLUDE_MENU = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "votesAmount");

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT_NEW_ID = 4;
    public static final int NOT_FOUND = 20;
    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "Entity with id=20 not found";

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Сопка", null);
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Сациви", null);
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Густав и Густав", null);
    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3);

    static {
        RESTAURANT1.setMenus(List.of(MENU1));
        RESTAURANT2.setMenus(List.of(MENU2));
        RESTAURANT3.setMenus(List.of(MENU3));
    }

    public static RestaurantTo getTo(Restaurant restaurant) {
        RestaurantTo restaurantTo = createTo(restaurant, 0L);
        for (Menu menu : restaurantTo.getMenus()) {
            for (Dish dish : menu.getDishes()) {
                dish.setMenu(null);
            }
            menu.setRestaurant(null);
        }
        return restaurantTo;
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        List<RestaurantTo> restaurantTos = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantTos.add(getTo(restaurant));
        }
        return restaurantTos;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "NewRest", null);
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, RESTAURANT1.getName(), null);
        updated.setName("UpdatedName");
        return updated;
    }
}
