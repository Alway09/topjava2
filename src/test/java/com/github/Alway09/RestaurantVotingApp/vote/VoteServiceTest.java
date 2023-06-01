package com.github.Alway09.RestaurantVotingApp.vote;

import com.github.Alway09.RestaurantVotingApp.TestData;
import com.github.Alway09.RestaurantVotingApp.error.IllegalRequestDataException;
import com.github.Alway09.RestaurantVotingApp.error.NotFoundException;
import com.github.Alway09.RestaurantVotingApp.model.Vote;
import com.github.Alway09.RestaurantVotingApp.service.VoteService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.time.LocalTime;
import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.*;
import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.*;
import static com.github.Alway09.RestaurantVotingApp.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VoteServiceTest {
    @Autowired
    private VoteService service;

    @Autowired
    CacheManager cacheManager;

    void setVotingTimes(long startDelta, long endDelta) {
        setVotingStart(LocalTime.now().plusHours(startDelta));
        setVotingEnd(LocalTime.now().plusHours(endDelta));
    }

    @Order(1)
    @Test
    void failureDelete_notFound() {
        setVotingTimes(-1, 1);
        assertThrows(NotFoundException.class, () -> service.deleteActualVote(user2.id()));
    }

    @Order(2)
    @Test
    void failureDelete_timeExceed() {
        setVotingTimes(-2, -1);
        assertThrows(IllegalRequestDataException.class, () -> service.deleteActualVote(user2.id()));
    }

    @Order(3)
    @Test
    void failureUpdate() {
        setVotingTimes(-2, -1);
        assertThrows(IllegalRequestDataException.class, () -> service.createOrUpdate(TestData.RESTAURANT1, user));
    }

    @Order(4)
    @Test
    void getActualVote() {
        assertEquals(1, service.getActualVote(user.id()).getRestaurant().getId());
        assertEquals(1, service.getActualVote(admin.id()).getRestaurant().getId());
    }

    @Order(5)
    @Test
    void getAllUserVotes() {
        List<Vote> votes = service.getAllUserVotes(user.id());
        assertEquals(USER_ALL_VOTES_AMOUNT, votes.size());
        assertEquals(votes, cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID, List.class));
    }

    @Order(6)
    @Test
    void successUpdate() {
        setVotingTimes(-1, 1);
        assertEquals(VOTE5, service.createOrUpdate(TestData.RESTAURANT1, user));
        assertEquals(VOTE5, service.getActualVote(user.id()));
        assertNull((cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID)));
    }

    @Order(7)
    @Test
    void successCreate() {
        assertEquals(NEW_VOTE, service.createOrUpdate(TestData.RESTAURANT1, user2));
        assertEquals(NEW_VOTE, service.getActualVote(user2.id()));
        assertNull((cacheManager.getCache(VOTES_CACHE_NAME).get(user2.id())));
    }

    @Order(8)
    @Test
    void successDelete() {
        setVotingTimes(-1, 1);
        service.deleteActualVote(user.id());
        assertNull((cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID)));
    }
}
