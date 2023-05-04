package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.restaurant.RestaurantTestData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.VoteUtil.createTos;
import static ru.javaops.topjava2.web.TestData.NOT_FOUND_EXCEPTION_MESSAGE;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;
import static ru.javaops.topjava2.web.vote.VoteTestData.*;

public class VoteUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteUserController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllVotesAmount() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_AMOUNT_TO_MATCHER.contentJson(createTos(Map.of(RESTAURANT1_ID, RESTAURANT1_VOTES_AMOUNT, RESTAURANT2_ID, RESTAURANT2_VOTES_AMOUNT))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllVotesAmountBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDateTime", LocalDate.now().atStartOfDay().toString())
                .param("endDateTime", LocalDate.now().atStartOfDay().plusHours(24).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_AMOUNT_TO_MATCHER.contentJson(createTos(Map.of(RESTAURANT1_ID, 2L, RESTAURANT2_ID, 0L))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getVotesAmount() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LONG_MATCHER.contentJson(RESTAURANT1_VOTES_AMOUNT));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getVotesAmount_restaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getVotesAmountBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .param("startDateTime", LocalDate.now().atStartOfDay().toString())
                .param("endDateTime", LocalDate.now().atStartOfDay().plusHours(24).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LONG_MATCHER.contentJson(2L));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
