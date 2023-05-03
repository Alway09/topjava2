package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.util.VoteUtil;
import ru.javaops.topjava2.web.AbstractControllerTest;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.TestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;

public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {
    protected final String restURL;

    public AbstractRestaurantControllerTest(String restURL) {
        this.restURL = restURL + "/";
    }

    @Test
    abstract void getAll() throws Exception;

    @Test
    abstract void getByName() throws Exception;

    @Test
    abstract void get() throws Exception;

    @Test
    void getList() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_EXCLUDE_MENU.contentJson(List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3)));
    }

    @Test
    void getByName_notExisted() throws Exception {
        getByName("NotExist", List.of());
    }

    protected void getByName(String name, List<RestaurantTo> expect) throws Exception {
        perform(MockMvcRequestBuilders.get(restURL).param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(expect));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(restURL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    void vote() throws Exception {
        if (VoteUtil.isVotingInProcess()) {
            perform(MockMvcRequestBuilders.put(restURL + RESTAURANT1_ID + "/vote"))
                    .andDo(print())
                    .andExpect(status().isOk());
        } else {
            perform(MockMvcRequestBuilders.put(restURL + RESTAURANT1_ID + "/vote"))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(VoteService.VOTING_NOT_COINCIDENCE_MESSAGE)));
        }
    }
}
