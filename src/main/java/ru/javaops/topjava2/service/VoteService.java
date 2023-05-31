package ru.javaops.topjava2.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.error.IllegalRequestDataException;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.javaops.topjava2.util.VoteUtil.*;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = VOTES_CACHE_NAME)
public class VoteService {
    VoteRepository repository;

    public static final String VOTING_NOT_COINCIDENCE_MESSAGE = "Voting time is out.";

    @CacheEvict(key = "#user.id()")
    public Vote createOrUpdate(Restaurant restaurant, User user) throws IllegalArgumentException {
        Objects.requireNonNull(restaurant);
        Optional<Vote> vote = repository.findUserVote(user.id(), LocalDate.now());
        if(vote.isPresent()){
            if (isVotingInProcess()) {
                log.info("updating vote for restaurant {}, userId={}", restaurant, user.id());
                vote.get().setRestaurant(restaurant);
                vote.get().setDate(LocalDate.now());
                return repository.save(vote.get());
            } else {
                throw new IllegalRequestDataException(VOTING_NOT_COINCIDENCE_MESSAGE + " " + getActualStartAndDateTimeMessage());
            }
        }

        return create(restaurant, user);
    }

    public Vote create(Restaurant restaurant, User user) {
        log.info("creating vote for restaurant {}, userId={}", restaurant, user.id());
        Vote newVote = new Vote(null, user, restaurant, LocalDate.now());
        return repository.save(newVote);
    }

    public Vote getActualVote(int userId) throws NotFoundException {
        Optional<Vote> vote = repository.findUserVote(userId, LocalDate.now());
        if (vote.isPresent()) {
            return vote.get();
        }
        throw new NotFoundException("Actual vote not found");
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

    @CacheEvict(key = "#userId")
    public void deleteActualVote(int userId) throws IllegalArgumentException {
        if(isVotingInProcess()){
            repository.deleteExisted(getActualVote(userId).id());
        } else {
            throw new IllegalRequestDataException(VOTING_NOT_COINCIDENCE_MESSAGE + " " + getActualStartAndDateTimeMessage());
        }
    }
}