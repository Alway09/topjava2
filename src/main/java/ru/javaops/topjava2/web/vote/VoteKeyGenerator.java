package ru.javaops.topjava2.web.vote;

import org.springframework.cache.interceptor.KeyGenerator;
import ru.javaops.topjava2.web.AuthUser;

import java.lang.reflect.Method;

public class VoteKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return AuthUser.authId();
    }
}
