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
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;

public abstract class AbstractRestaurantControllerTest extends AbstractControllerTest {
    protected final String restURL;

    public AbstractRestaurantControllerTest(String restURL) {
        this.restURL = restURL + "/";
    }

    protected void getByName(String name, List<RestaurantTo> expect) throws Exception {
        perform(MockMvcRequestBuilders.get(restURL).param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER_EXCLUDE_VOTES_AMOUNT.contentJson(expect));
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
