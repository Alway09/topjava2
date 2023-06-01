package com.github.Alway09.RestaurantVotingApp.restaurant;

import com.github.Alway09.RestaurantVotingApp.AbstractControllerTest;
import com.github.Alway09.RestaurantVotingApp.TestData;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.to.RestaurantTo;
import com.github.Alway09.RestaurantVotingApp.web.restaurant.RestaurantController;
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
                .andExpect(RestaurantTestData.RESTAURANT_TO_MATCHER.contentJson(RestaurantTestData.getTos(restaurantList)));

        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getAllWithActualMenus", List.class),
                List.of(TestData.RESTAURANT1, TestData.RESTAURANT2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getList() throws Exception {
        List<Restaurant> restaurantList = new ArrayList<>(List.of(TestData.RESTAURANT2, TestData.RESTAURANT1));
        perform(MockMvcRequestBuilders.get(REST_URL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(restaurantList));

        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurantList,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getListWithActualMenus", List.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_TO_MATCHER.contentJson(RestaurantTestData.getTo(TestData.RESTAURANT1)));

        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(TestData.RESTAURANT1,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get(RestaurantTestData.RESTAURANT1_ID, Restaurant.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get_notActual() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByName() throws Exception {
        getByName(TestData.RESTAURANT1.getName(), List.of(RestaurantTestData.getTo(TestData.RESTAURANT1)));
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(TestData.RESTAURANT1,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get(TestData.RESTAURANT1.getName(), Restaurant.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByName_notActual() throws Exception {
        getByName(TestData.RESTAURANT3.getName(), List.of());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByName_notExisted() throws Exception {
        getByName("NotExist", List.of());
    }

    protected void getByName(String name, List<RestaurantTo> expect) throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL).param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_TO_MATCHER.contentJson(expect));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
