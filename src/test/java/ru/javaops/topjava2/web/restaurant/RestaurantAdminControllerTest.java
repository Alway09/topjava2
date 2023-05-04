package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.util.JsonUtil;
import ru.javaops.topjava2.web.AbstractControllerTest;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.TestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.getUpdated;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

public class RestaurantAdminControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantAdminController.REST_URL + "/";

    @Autowired
    RestaurantRepository repository;

    // ===============================CREATE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        create(newRestaurant, status().isCreated());

        newRestaurant.setId(RESTAURANT_NEW_ID);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(RESTAURANT_NEW_ID), newRestaurant);
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
                .content(JsonUtil.writeValue(getInvalid())))
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
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTos(List.of(RESTAURANT3, RESTAURANT2, RESTAURANT1))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getList() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTo(RESTAURANT3)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByName() throws Exception {
        getByName(RESTAURANT3.getName(), List.of(getTo(RESTAURANT3)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
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
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateUserAuth() throws Exception {
        update(getUpdated(), status().isForbidden());
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
        assertFalse(repository.findById(RESTAURANT2_ID).isPresent());
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
