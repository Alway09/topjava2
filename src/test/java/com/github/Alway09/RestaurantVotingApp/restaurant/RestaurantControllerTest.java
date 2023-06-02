package com.github.Alway09.RestaurantVotingApp.restaurant;

import com.github.Alway09.RestaurantVotingApp.AbstractControllerTest;
import com.github.Alway09.RestaurantVotingApp.TestData;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.web.restaurant.RestaurantController;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.USER_MAIL;
import static com.github.Alway09.RestaurantVotingApp.util.RestaurantUtil.RESTAURANTS_CACHE_NAME;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(1)
public class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Autowired
    private CacheManager cacheManager;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        List<Restaurant> restaurantList = new ArrayList<>(List.of(TestData.RESTAURANT2, TestData.RESTAURANT1));;

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(restaurantList));

        RestaurantTestData.RESTAURANT_MATCHER_EXCLUDE_MENU.assertMatch(List.of(TestData.RESTAURANT2, TestData.RESTAURANT1),
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getAllWithActualMenus", List.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(TestData.RESTAURANT1));

        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(TestData.RESTAURANT1,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get(RestaurantTestData.RESTAURANT1_ID, Restaurant.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get_notActual() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
