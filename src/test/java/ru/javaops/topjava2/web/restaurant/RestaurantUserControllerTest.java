package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.TestData.*;
import static ru.javaops.topjava2.web.TestData.RESTAURANT3;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

@WithUserDetails(value = USER_MAIL)
public class RestaurantUserControllerTest extends AbstractRestaurantControllerTest {
    public RestaurantUserControllerTest() {
        super(RestaurantUserController.REST_URL);
    }

    @Override
    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTos(List.of(RESTAURANT2, RESTAURANT1))));
    }

    @Override
    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(getTo(RESTAURANT1)));
    }

    @Test
    void get_notActual() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL + RESTAURANT3_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Entity with id=" + RESTAURANT3_ID + " not found")));
    }

    @Override
    @Test
    void getByName() throws Exception {
        getByName(RESTAURANT1.getName(), List.of(getTo(RESTAURANT1)));
    }

    @Test
    void getByName_notActual() throws Exception {
        getByName(RESTAURANT3.getName(), List.of());
    }
}
