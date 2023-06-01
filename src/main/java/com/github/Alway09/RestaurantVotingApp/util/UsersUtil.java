package com.github.Alway09.RestaurantVotingApp.util;

import com.github.Alway09.RestaurantVotingApp.model.Role;
import com.github.Alway09.RestaurantVotingApp.model.User;
import com.github.Alway09.RestaurantVotingApp.to.UserTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsersUtil {
    public static final String PROFILE_CACHE_NAME = "profile";

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}