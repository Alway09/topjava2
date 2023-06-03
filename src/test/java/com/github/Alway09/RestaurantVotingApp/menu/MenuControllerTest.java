package com.github.Alway09.RestaurantVotingApp.menu;

import com.github.Alway09.RestaurantVotingApp.AbstractControllerTest;
import com.github.Alway09.RestaurantVotingApp.to.MenuOutTo;
import com.github.Alway09.RestaurantVotingApp.web.menu.MenuController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.Alway09.RestaurantVotingApp.TestData.MENU1;
import static com.github.Alway09.RestaurantVotingApp.TestData.NOT_FOUND_EXCEPTION_MESSAGE;
import static com.github.Alway09.RestaurantVotingApp.menu.MenuTestData.*;
import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.USER_MAIL;
import static com.github.Alway09.RestaurantVotingApp.util.MenuUtil.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MenuController.REST_URL + "/";
    @Autowired
    private CacheManager cacheManager;
    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_OUT_TO_MATCHER.contentJson(createOutTo(MENU1)));

        assertEquals(createOutTo(MENU1), cacheManager.getCache(MENUS_CACHE_NAME).get(MENU1_ID, MenuOutTo.class));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(NOT_FOUND_EXCEPTION_MESSAGE)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
