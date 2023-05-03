package ru.javaops.topjava2.web.restaurant;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.web.TestData.RESTAURANT1;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER_EXCLUDE_MENU = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "votesAmount");

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT_NEW_ID = 4;
    public static final int NOT_FOUND = 20;

    public static RestaurantTo getTo(Restaurant restaurant) {
        Restaurant restaurantCopy = new Restaurant(restaurant);

        List<Menu> menusCopy = new ArrayList<>();
        for (Menu menu : restaurantCopy.getMenus()) {
            menusCopy.add(new Menu(menu.getId(), menu.getName(), null, menu.getDishes(), menu.getCreationDate()));
        }

        restaurantCopy.setMenus(menusCopy);
        return createTo(restaurantCopy, 0L);
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
