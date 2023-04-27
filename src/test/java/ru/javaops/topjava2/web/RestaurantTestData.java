package ru.javaops.topjava2.web;

import ru.javaops.topjava2.model.Restaurant;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Сопка", null);
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Сациви", null);
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Густав и густав", null);
}
