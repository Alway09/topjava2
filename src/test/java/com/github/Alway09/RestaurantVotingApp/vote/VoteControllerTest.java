package com.github.Alway09.RestaurantVotingApp.vote;

import com.github.Alway09.RestaurantVotingApp.AbstractControllerTest;
import com.github.Alway09.RestaurantVotingApp.TestData;
import com.github.Alway09.RestaurantVotingApp.restaurant.RestaurantTestData;
import com.github.Alway09.RestaurantVotingApp.util.JsonUtil;
import com.github.Alway09.RestaurantVotingApp.util.VoteUtil;
import com.github.Alway09.RestaurantVotingApp.web.VoteController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.restaurant.RestaurantTestData.*;
import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.USER2_MAIL;
import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.USER_MAIL;
import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.createTo;
import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.createTos;
import static com.github.Alway09.RestaurantVotingApp.vote.VoteTestData.VOTE_TO_MATCHER_EXCLUDE_DATE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER_EXCLUDE_DATE
                        .contentJson(createTos(List.of(TestData.VOTE5, TestData.VOTE4, TestData.VOTE3, TestData.VOTE2, TestData.VOTE1))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurant")
                .param("restaurantId", String.valueOf(RestaurantTestData.RESTAURANT1_ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER_EXCLUDE_DATE
                        .contentJson(createTos(List.of(TestData.VOTE5, TestData.VOTE3, TestData.VOTE2, TestData.VOTE1))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void getAllForRestaurant_restaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurant")
                .param("restaurantId", String.valueOf(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(TestData.NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getActualVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "actual"))
                .andDo(print())
                .andExpect(VOTE_TO_MATCHER_EXCLUDE_DATE.contentJson(createTo(TestData.VOTE5)));

    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    public void create() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT3_ID)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void create_voteExist() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT1_ID)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Actual vote for user")))
                .andExpect(content().string(containsString("already exist.")));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void create_restaurantNotExist() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void update() throws Exception {
        if(VoteUtil.isVotingInProcess()){
            perform(MockMvcRequestBuilders.put(REST_URL + "vote")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(RESTAURANT1_ID)))
                    .andDo(print())
                    .andExpect(status().isOk());
        } else {
            perform(MockMvcRequestBuilders.put(REST_URL + "vote")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(RESTAURANT1_ID)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString("Voting time is out.")));
        }
    }
}
