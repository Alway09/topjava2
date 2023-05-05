package ru.javaops.topjava2.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.error.IllegalRequestDataException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.util.VoteUtil;
import ru.javaops.topjava2.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;
import static ru.javaops.topjava2.util.VoteUtil.*;
import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.web.AuthUser.authId;
import static ru.javaops.topjava2.web.AuthUser.authUser;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
    VoteRepository repository;

    public static final String VOTING_NOT_COINCIDENCE_MESSAGE = "Voting time is out.";
    private static final LocalDateTime MIN_DATE_TIME = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
    private static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(3000, 1, 1, 0, 0, 0);

    public void createOrUpdate(Restaurant restaurant) {
        Objects.requireNonNull(restaurant);
        if (VoteUtil.isVotingInProcess()) {
            Optional<Vote> vote = repository.findUserVote(authId(), votingStart(), votingEnd());
            if (vote.isPresent()) {
                log.info("updating vote for restaurant {}, userId={}", restaurant, authId());
                vote.get().setRestaurant(restaurant);
                vote.get().setDateTime(LocalDateTime.now());
                repository.save(vote.get());
            } else {
                log.info("creating vote for restaurant {}, userId={}", restaurant, authId());
                Vote newVote = new Vote(null, authUser(), restaurant, LocalDateTime.now());
                repository.save(newVote);
            }
            return;
        }

        throw new IllegalRequestDataException(VOTING_NOT_COINCIDENCE_MESSAGE + " " + getActualStartAndDateTimeMessage());
    }

    public long getVotesAmountBetweenInclusive(int restaurantId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        startDateTime = requireNonNullElse(startDateTime, MIN_DATE_TIME);
        endDateTime = requireNonNullElse(endDateTime, MAX_DATE_TIME);
        log.info("get votes amount for restaurant id={} for user id={} between {} and {}", restaurantId, authId(), startDateTime, endDateTime);
        return repository.getVotesAmountBetweenInclusive(restaurantId, startDateTime, endDateTime);
    }

    public long getActualVotesAmount(int restaurantId) {
        return getVotesAmountBetweenInclusive(restaurantId, votingStart(), votingEnd());
    }

    public Map<Integer, Long> getVotesAmountOfAllRestaurantsBetweenInclusive(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        startDateTime = requireNonNullElse(startDateTime, MIN_DATE_TIME);
        endDateTime = requireNonNullElse(endDateTime, MAX_DATE_TIME);
        log.info("get votes amount for all restaurants for user id={} between {} and {}", authId(), startDateTime, endDateTime);
        return repository.getVotesAmountOfAllRestaurantsBetweenInclusive(startDateTime, endDateTime);
    }

    public Map<Integer, Long> getActualVotesAmountOfAllRestaurants() {
        return getVotesAmountOfAllRestaurantsBetweenInclusive(votingStart(), votingEnd());
    }

    public List<Vote> getAllUserVotes() {
        log.info("get all votes of user id={}", authId());
        return repository.getAllUserVotes(authId());
    }

    public List<Vote> getAllUserVotesForRestaurant(int restaurantId) {
        log.info("get votes of user id={} for restaurant  id={}", authId(), restaurantId);
        return repository.getAllUserVotesForRestaurant(AuthUser.authId(), restaurantId);
    }

    public void deleteActualVote() {
        Optional<Vote> vote = repository.findUserVote(authId(), votingStart(), votingEnd());
        if (vote.isPresent()) {
            repository.deleteExisted(vote.get().id());
        } else {
            throw new IllegalRequestDataException(VOTING_NOT_COINCIDENCE_MESSAGE + " " + getActualStartAndDateTimeMessage());
        }
    }
}