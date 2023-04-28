package ru.javaops.topjava2.web.restaurant;

import org.springframework.security.test.context.support.WithUserDetails;

import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

@WithUserDetails(value = USER_MAIL)
public class RestaurantUserControllerTest extends AbstractRestaurantControllerTest {
    public RestaurantUserControllerTest() {
        super(RestaurantUserController.REST_URL);
    }
}
