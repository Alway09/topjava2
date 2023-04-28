package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RestaurantUtil {
    public static RestaurantTo createTo(Restaurant restaurant, Long votesAmount){
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenus(), votesAmount);
    }

    public static List<RestaurantTo> createTos(Collection<Restaurant> restaurants, Map<Integer, Long> restaurantVotes){
        return restaurants.stream().map(restaurant -> createTo(restaurant, restaurantVotes.getOrDefault(restaurant.getId(), 0L))).toList();
    }
}
