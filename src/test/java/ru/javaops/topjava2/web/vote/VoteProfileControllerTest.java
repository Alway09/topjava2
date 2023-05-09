package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.util.VoteUtil;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.restaurant.RestaurantTestData;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.VoteUtil.createTo;
import static ru.javaops.topjava2.util.VoteUtil.createTos;
import static ru.javaops.topjava2.web.TestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT3_ID;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;
import static ru.javaops.topjava2.web.vote.VoteTestData.VOTE_TO_MATCHER_EXCLUDE_DATE_TIME;

public class VoteProfileControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteProfileController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER_EXCLUDE_DATE_TIME
                        .contentJson(createTos(List.of(VOTE5, VOTE4, VOTE3, VOTE2, VOTE1))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER_EXCLUDE_DATE_TIME
                        .contentJson(createTos(List.of(VOTE5, VOTE3, VOTE2, VOTE1))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void getAllForRestaurant_restaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getActualVote() throws Exception {
        if (VoteUtil.isVotingInProcess()) {
            perform(MockMvcRequestBuilders.get(REST_URL + "actual"))
                    .andDo(print())
                    .andExpect(VOTE_TO_MATCHER_EXCLUDE_DATE_TIME.contentJson(createTo(VOTE5)));
        } else {
            perform(MockMvcRequestBuilders.get(REST_URL + "actual"))
                    .andDo(print())
                    .andExpect(content().string(containsString("Actual vote not found")));
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void voteForRestaurant() throws Exception {
        if (VoteUtil.isVotingInProcess()) {
            perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT3_ID))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        } else {
            perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT3_ID))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(VoteService.VOTING_NOT_COINCIDENCE_MESSAGE)));
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void voteForRestaurant_restaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteActualVote() throws Exception {
        if (VoteUtil.isVotingInProcess()) {
            perform(MockMvcRequestBuilders.delete(REST_URL))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        } else {
            perform(MockMvcRequestBuilders.delete(REST_URL))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Actual vote not found")));
        }

    }
}
