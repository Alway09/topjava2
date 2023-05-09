package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.AbstractControllerTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.RestaurantUtil.RESTAURANTS_CACHE_NAME;
import static ru.javaops.topjava2.util.VoteUtil.isVotingInProcess;
import static ru.javaops.topjava2.web.TestData.*;
import static ru.javaops.topjava2.web.TestData.RESTAURANT3;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

public class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Autowired
    CacheManager cacheManager;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        List<Restaurant> restaurantList;
        if (isVotingInProcess()) {
            restaurantList = new ArrayList<>(List.of(RESTAURANT1, RESTAURANT2));
        } else {
            restaurantList = new ArrayList<>(List.of(RESTAURANT2, RESTAURANT1));
        }
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTos(restaurantList)));

        RESTAURANT_MATCHER.assertMatch(cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getAllWithActualMenus", List.class),
                List.of(RESTAURANT1, RESTAURANT2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getList() throws Exception {
        List<Restaurant> restaurantList = new ArrayList<>(List.of(RESTAURANT2, RESTAURANT1));
        perform(MockMvcRequestBuilders.get(REST_URL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(restaurantList));

        RESTAURANT_MATCHER.assertMatch(restaurantList,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getListWithActualMenus", List.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTo(RESTAURANT1)));

        RESTAURANT_MATCHER.assertMatch(RESTAURANT1,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get(RESTAURANT1_ID, Restaurant.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get_notActual() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByName() throws Exception {
        getByName(RESTAURANT1.getName(), List.of(getTo(RESTAURANT1)));
        RESTAURANT_MATCHER.assertMatch(RESTAURANT1,
                cacheManager.getCache(RESTAURANTS_CACHE_NAME).get(RESTAURANT1.getName(), Restaurant.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByName_notActual() throws Exception {
        getByName(RESTAURANT3.getName(), List.of());
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
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(expect));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
