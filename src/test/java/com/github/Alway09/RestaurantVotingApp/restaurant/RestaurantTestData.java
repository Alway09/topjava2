package com.github.Alway09.RestaurantVotingApp.restaurant;

import com.github.Alway09.RestaurantVotingApp.MatcherFactory;
import com.github.Alway09.RestaurantVotingApp.TestData;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.to.RestaurantTo;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT_NEW_ID = 4;
    public static final int NOT_FOUND = 20;

    public static Restaurant getNew() {
        return new Restaurant(null, "NewRest", null);
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, TestData.RESTAURANT1.getName(), null);
        updated.setName("UpdatedName");
        return updated;
    }

    public static Restaurant getUpdatedInvalid() {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, TestData.RESTAURANT1.getName(), null);
        updated.setName(null);
        return updated;
    }

    public static RestaurantTo getNewInvalid() {
        return new RestaurantTo(null, null);
    }
}
