package com.github.Alway09.RestaurantVotingApp.service;

import com.github.Alway09.RestaurantVotingApp.error.DataConflictException;
import com.github.Alway09.RestaurantVotingApp.error.IllegalRequestDataException;
import com.github.Alway09.RestaurantVotingApp.error.NotFoundException;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.model.User;
import com.github.Alway09.RestaurantVotingApp.model.Vote;
import com.github.Alway09.RestaurantVotingApp.repository.VoteRepository;
import com.github.Alway09.RestaurantVotingApp.util.VoteUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.*;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = VOTES_CACHE_NAME)
public class VoteService {
    VoteRepository repository;

    public static final String VOTING_NOT_COINCIDENCE_MESSAGE = "Voting time is out.";

    @CacheEvict(key = "#userId")
    public Vote update(Restaurant restaurant, int userId) throws IllegalArgumentException, NotFoundException {
        Objects.requireNonNull(restaurant);
        Optional<Vote> vote = repository.findUserVote(userId, LocalDate.now());
        if(vote.isPresent()){
            if (VoteUtil.votingTime.isVotingInProcess()) {
                log.info("updating vote for restaurant {}, userId={}", restaurant, userId);
                vote.get().setRestaurant(restaurant);
                vote.get().setDate(LocalDate.now());
                return repository.save(vote.get());
            } else {
                throw new IllegalRequestDataException(VOTING_NOT_COINCIDENCE_MESSAGE + " " + VoteUtil.votingTime.getActualTimeMessage());
            }
        }

        throw new NotFoundException("Actual vote not found");
    }

    public Vote create(Restaurant restaurant, User user) {
        log.info("creating vote for restaurant {}, userId={}", restaurant, user.id());
        Optional<Vote> vote = repository.findUserVote(user.id(), LocalDate.now());
        if(vote.isPresent()){
            throw new DataConflictException("Actual vote for user " + user + " already exist.");
        }
        Vote newVote = new Vote(null, user, restaurant, LocalDate.now());
        return repository.save(newVote);
    }

    public Vote getActualVote(int userId) throws NotFoundException {
        Optional<Vote> vote = repository.findUserVote(userId, LocalDate.now());
        return vote.orElseThrow(() -> new NotFoundException("Actual vote not found"));
    }

    @Cacheable(key = "#userId")
    public List<Vote> getAllUserVotes(int userId) {
        log.info("get all votes of user id={}", userId);
        return repository.getAllUserVotes(userId);
    }

    public List<Vote> getAllUserVotesForRestaurant(int restaurantId, int userId) {
        log.info("get votes of user id={} for restaurant  id={}", userId, restaurantId);
        return repository.getAllUserVotesForRestaurant(userId, restaurantId);
    }
}