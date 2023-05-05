package ru.javaops.topjava2.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.util.JsonUtil;
import ru.javaops.topjava2.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.MenuUtil.*;
import static ru.javaops.topjava2.web.TestData.*;
import static ru.javaops.topjava2.web.menu.MenuTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

public class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MenuController.REST_URL + "/";

    @Autowired
    MenuRepository repository;

    // ===============================CREATE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newMenu = getNew();
        create(newMenu, status().isCreated());

        newMenu.setId(MENU_NEW_ID);
        MENU_MATCHER.assertMatch(repository.getExisted(MENU_NEW_ID), createFromTo(newMenu));
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
                .andExpect(content().string(containsString("MenuTo must be new (id=null)")));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewInvalid1())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"restaurantId\":\"must not be null\"")))
                .andExpect(content().string(containsString("\"dishes\":\"must not be null\"")))
                .andExpect(content().string(containsString("\"name\":\"must not be blank\"")));

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewInvalid2())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"restaurantId\":\"must be greater than or equal to 1\"")))
                .andExpect(content().string(containsString("\"name\":\"size must be between 2 and 128\"")))
                .andExpect(content().string(containsString("\"creationDate\":\"Creation date must be current or in future\"")));

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewInvalid3())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"restaurantId\":\"must be greater than or equal to 1\"")))
                .andExpect(content().string(containsString("\"dishes[0].name\":\"must not be blank\"")))
                .andExpect(content().string(containsString("\"dishes[0].price\":\"must be greater than or equal to 0\"")));
    }

    private void create(MenuTo menuTo, ResultMatcher result) throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo)))
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
                .andExpect(MENU_TO_MATCHER.contentJson(createTos(List.of(MENU2, MENU1, MENU3))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", LocalDate.now().toString())
                .param("endDate", LocalDate.now().plusDays(1).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(createTos(List.of(MENU2, MENU1))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(createTo(MENU1)));
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
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ===============================UPDATE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo updated = getUpdated();
        update(updated, status().isNoContent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        MenuTo updated = getUpdated();
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

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedInvalid1())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"dishes\":\"must not be null\"")))
                .andExpect(content().string(containsString("\"name\":\"must not be blank\"")));

        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedInvalid2())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"dishes[0].name\":\"must not be blank\"")))
                .andExpect(content().string(containsString("\"name\":\"size must be between 2 and 128\"")))
                .andExpect(content().string(containsString("\"creationDate\":\"Creation date must be current or in future\"")));

        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedInvalid3())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("\"dishes[0].name\":\"must not be blank\"")))
                .andExpect(content().string(containsString("\"dishes[0].price\":\"must be greater than or equal to 0\"")));
    }

    private void update(MenuTo menuTo, ResultMatcher result) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo)))
                .andDo(print())
                .andExpect(result);

        MENU_MATCHER.assertMatch(repository.getExisted(MENU1_ID), createFromTo(menuTo));
    }

    // ===============================DELETE===============================
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU2_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(MENU2_ID).isPresent());
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
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU1_ID))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
