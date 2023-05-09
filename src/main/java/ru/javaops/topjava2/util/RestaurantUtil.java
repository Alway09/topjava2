package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.CreateRestaurantTo;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RestaurantUtil {
    public static final String RESTAURANTS_CACHE_NAME = "restaurants";

    public static RestaurantTo createTo(Restaurant restaurant, Long votesAmount) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenus(), votesAmount);
    }

    public static CreateRestaurantTo createTo(Restaurant restaurant) {
        return new CreateRestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static List<RestaurantTo> createTos(Collection<Restaurant> restaurants, Map<Integer, Long> restaurantVotes) {
        return restaurants.stream()
                .map(restaurant -> createTo(restaurant, restaurantVotes.getOrDefault(restaurant.getId(), 0L)))
                .sorted(Comparator.comparing(RestaurantTo::getVotesAmount).reversed()
                        .thenComparing(RestaurantTo::getName))
                .toList();
    }

    public static List<CreateRestaurantTo> createTos(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).toList();
    }

    public static Restaurant createFromTo(CreateRestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName(), null);
    }
}
