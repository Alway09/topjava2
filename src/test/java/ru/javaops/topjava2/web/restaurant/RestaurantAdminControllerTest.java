package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.util.JsonUtil;

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

@WithUserDetails(value = ADMIN_MAIL)
public class RestaurantAdminControllerTest extends AbstractRestaurantControllerTest {
    @Autowired
    RestaurantRepository repository;

    public RestaurantAdminControllerTest() {
        super(RestaurantAdminController.REST_URL);
    }

    // ===============================CREATE===============================
    @Test
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
    void createNotNew() throws Exception {
        perform(MockMvcRequestBuilders.post(restURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdated())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("CreateRestaurantTo must be new (id=null)")));
    }

    private void create(Restaurant restaurant, ResultMatcher result) throws Exception {
        perform(MockMvcRequestBuilders.post(restURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andDo(print())
                .andExpect(result);
    }

    // ===============================READ===============================
    @Override
    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTos(List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3))));
    }

    @Override
    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL + RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTo(RESTAURANT3)));
    }

    @Override
    @Test
    void getByName() throws Exception {
        getByName(RESTAURANT3.getName(), List.of(getTo(RESTAURANT3)));
    }

    // ===============================UPDATE===============================
    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        update(updated, status().isNoContent());

        perform(MockMvcRequestBuilders.get(restURL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(updated));
    }

    @Test
    void updateNotFound() throws Exception {
        Restaurant updated = getUpdated();
        updated.setId(NOT_FOUND);
        perform(MockMvcRequestBuilders.put(restURL + NOT_FOUND)
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
        perform(MockMvcRequestBuilders.put(restURL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andDo(print())
                .andExpect(result);
    }

    // ===============================DELETE===============================
    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(restURL + RESTAURANT2_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(RESTAURANT2_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(restURL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteUserAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(restURL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
