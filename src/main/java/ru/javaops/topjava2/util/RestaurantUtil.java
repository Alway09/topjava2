package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.CreateRestaurantTo;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class RestaurantUtil {
    public static final String RESTAURANTS_CACHE_NAME = "restaurants";

    public static RestaurantTo createOutcomeTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenus());
    }

    public static CreateRestaurantTo createIncomeTo(Restaurant restaurant) {
        return new CreateRestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static List<RestaurantTo> createOutcomeTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantUtil::createOutcomeTo)
                .sorted(Comparator.comparing(RestaurantTo::getName))
                .toList();
    }

    public static List<CreateRestaurantTo> createIncomeTos(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createIncomeTo).toList();
    }

    public static Restaurant createFromTo(CreateRestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName(), null);
    }
}
