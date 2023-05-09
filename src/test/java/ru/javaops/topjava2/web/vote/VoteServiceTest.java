package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.error.IllegalRequestDataException;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.web.restaurant.RestaurantTestData;
import ru.javaops.topjava2.web.user.UserTestData;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.topjava2.util.VoteUtil.*;
import static ru.javaops.topjava2.web.TestData.RESTAURANT1;
import static ru.javaops.topjava2.web.TestData.RESTAURANT2;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaops.topjava2.web.user.UserTestData.USER_ID;
import static ru.javaops.topjava2.web.vote.VoteTestData.*;

@SpringBootTest
public class VoteServiceTest {
    @Autowired
    VoteService service;

    @Autowired
    CacheManager cacheManager;

    void setVotingTimes(long startDelta, long endDelta) {
        setVotingStart(LocalTime.now().plusHours(startDelta));
        setVotingEnd(LocalTime.now().plusHours(endDelta));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void successUpdate() {
        setVotingTimes(-1, 1);
        assertEquals(2, service.getActualVotesAmount(RESTAURANT1_ID));
        service.createOrUpdate(RESTAURANT2);
        assertEquals(1, service.getActualVotesAmount(RestaurantTestData.RESTAURANT2_ID));
        assertEquals(1, service.getActualVotesAmount(RESTAURANT1_ID));
        assertNull((cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID)));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER2_MAIL)
    void successCreate() {
        setVotingTimes(-1, 1);

        assertEquals(2, service.getActualVotesAmount(RESTAURANT1_ID));
        service.createOrUpdate(RESTAURANT1);
        assertEquals(3, service.getActualVotesAmount(RESTAURANT1_ID));
        assertNull((cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID)));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void failureCreateOrUpdate() {
        setVotingTimes(-2, -1);
        assertThrows(IllegalRequestDataException.class, () -> service.createOrUpdate(RESTAURANT1));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void getActualVotes() {
        setVotingTimes(-1, 1);
        assertEquals(2, service.getActualVotesAmount(RESTAURANT1_ID));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void getActualVote() {
        setVotingTimes(-1, 1);
        assertEquals(1, service.getActualVote().getRestaurant().getId());
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void getActualVotes_votingStartAndEndOutOfBounds() {
        setVotingTimes(-1, -2);
        assertEquals(0, service.getActualVotesAmount(RESTAURANT1_ID));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void getVotesAmountOfAllRestaurants() {
        setVotingTimes(-1, 1);
        Map<Integer, Long> votes = service.getVotesAmountOfAllRestaurantsBetweenInclusive(null, null);
        assertEquals(5, votes.get(RESTAURANT1_ID));
        assertEquals(1, votes.get(RestaurantTestData.RESTAURANT2_ID));
        service.createOrUpdate(RESTAURANT2);

        setVotingTimes(-2, -1);
        votes = service.getVotesAmountOfAllRestaurantsBetweenInclusive(null, null);
        assertEquals(4, votes.get(RESTAURANT1_ID));
        assertEquals(2, votes.get(RestaurantTestData.RESTAURANT2_ID));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void getActualVotesAmountOfRestaurants() {
        setVotingTimes(-1, 1);
        Map<Integer, Long> votes = service.getActualVotesAmountOfAllRestaurants();
        assertEquals(2, votes.get(RESTAURANT1_ID));
        assertEquals(0, votes.getOrDefault(RestaurantTestData.RESTAURANT2_ID, 0L));
        service.createOrUpdate(RESTAURANT2);

        votes = service.getActualVotesAmountOfAllRestaurants();
        assertEquals(1, votes.get(RESTAURANT1_ID));
        assertEquals(1, votes.get(RestaurantTestData.RESTAURANT2_ID));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void getAllUserVotes() {
        List<Vote> votes = service.getAllUserVotes();
        assertEquals(USER_ALL_VOTES_AMOUNT, votes.size());
        assertEquals(votes, cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID, List.class));
    }

    @Test
    @Transactional
    @WithUserDetails(UserTestData.USER_MAIL)
    void successDelete() {
        setVotingTimes(-1, 1);
        service.deleteActualVote();
        assertEquals(RESTAURANT1_VOTES_AMOUNT - 1, service.getVotesAmountBetweenInclusive(RESTAURANT1_ID, null, null));
        assertNull((cacheManager.getCache(VOTES_CACHE_NAME).get(USER_ID)));
    }

    @Test
    @WithUserDetails(UserTestData.USER_MAIL)
    void failureDelete() {
        setVotingTimes(-2, -1);
        assertThrows(NotFoundException.class, () -> service.deleteActualVote());
    }
}
