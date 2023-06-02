package com.github.Alway09.RestaurantVotingApp.restaurant;

import com.github.Alway09.RestaurantVotingApp.AbstractControllerTest;
import com.github.Alway09.RestaurantVotingApp.error.NotFoundException;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.service.RestaurantService;
import com.github.Alway09.RestaurantVotingApp.util.JsonUtil;
import com.github.Alway09.RestaurantVotingApp.web.restaurant.RestaurantAdminController;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.TestData.*;
import static com.github.Alway09.RestaurantVotingApp.restaurant.RestaurantTestData.*;
import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.ADMIN_MAIL;
import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.USER_MAIL;
import static com.github.Alway09.RestaurantVotingApp.util.RestaurantUtil.RESTAURANTS_CACHE_NAME;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(2)
public class RestaurantAdminControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantAdminController.REST_URL + "/";

    @Autowired
    private RestaurantService service;

    @Autowired
    CacheManager cacheManager;

    // ===============================CREATE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        create(newRestaurant, status().isCreated());

        newRestaurant.setId(RESTAURANT_NEW_ID);
        RESTAURANT_MATCHER.assertMatch(service.findById(RESTAURANT_NEW_ID), newRestaurant);
        assertNull(cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getAllWithActualMenus", List.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocationUserAuth() throws Exception {
        create(getNew(), status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotNew() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdated())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("CreateRestaurantTo must be new (id=null)")));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewInvalid())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"name\":\"must not be blank\"")));
    }

    private void create(Restaurant restaurant, ResultMatcher result) throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andDo(print())
                .andExpect(result);
    }

    // ===============================READ===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(List.of(RESTAURANT3, RESTAURANT2, RESTAURANT1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(getTo(RESTAURANT3)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Restaurant with id=" + NOT_FOUND + " not found")));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ===============================UPDATE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        update(updated, status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(updated));
        assertNull(cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getAllWithActualMenus", List.class));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        Restaurant updated = getUpdated();
        updated.setId(NOT_FOUND);
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Restaurant with id=" + NOT_FOUND + " not found")));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateUserAuth() throws Exception {
        update(getUpdated(), status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedInvalid())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"name\":\"must not be blank\"")));
    }

    private void update(Restaurant restaurant, ResultMatcher result) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andDo(print())
                .andExpect(result);
    }

    // ===============================DELETE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT2_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.findById(RESTAURANT2_ID));
        assertNull(cacheManager.getCache(RESTAURANTS_CACHE_NAME).get("getAllWithActualMenus", List.class));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteUserAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
