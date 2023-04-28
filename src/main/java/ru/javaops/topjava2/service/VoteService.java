package ru.javaops.topjava2.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.error.IllegalRequestDataException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.util.VoteUtil;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.javaops.topjava2.util.VoteUtil.*;
import static ru.javaops.topjava2.web.AuthUser.authId;
import static ru.javaops.topjava2.web.AuthUser.authUser;

@Service
@AllArgsConstructor
public class VoteService {
    VoteRepository repository;

    public static final String VOTING_NOT_COINCIDENCE_MESSAGE = "Voting time is out.";

    public void createOrUpdate(Restaurant restaurant) {
        Objects.requireNonNull(restaurant);
        if (VoteUtil.isVotingInProcess()) {
            Optional<Vote> vote = repository.findUserVote(authId(), votingStart(), votingEnd());
            if (vote.isPresent()) { // update
                vote.get().setRestaurant(restaurant);
                vote.get().setDateTime(LocalDateTime.now());
                repository.save(vote.get());
            } else { // create
                Vote newVote = new Vote(authUser(), restaurant, LocalDateTime.now());
                repository.save(newVote);
            }
            return;
        }

        throw new IllegalRequestDataException(VOTING_NOT_COINCIDENCE_MESSAGE + " " + getActualStartAndDateTimeMessage());
    }

    public long getActualVotes(int restaurantId) {
        return repository.getVotesBetweenInclusive(restaurantId, votingStart(), votingEnd());
    }

    public Map<Integer, Long> getActualVotesOfRestaurants() {
        return repository.getVotesOfAllRestaurantsBetweenInclusive(votingStart(), votingEnd());
    }

    public Map<Integer, Long> getVotesOfAllRestaurants() {
        return repository.getVotesOfAllRestaurants();
    }
}