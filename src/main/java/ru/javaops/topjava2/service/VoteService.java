package ru.javaops.topjava2.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.util.VoteUtil;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static ru.javaops.topjava2.util.VoteUtil.votingEnd;
import static ru.javaops.topjava2.util.VoteUtil.votingStart;
import static ru.javaops.topjava2.web.AuthUser.authId;
import static ru.javaops.topjava2.web.AuthUser.authUser;

@Service
@AllArgsConstructor
public class VoteService {
    VoteRepository repository;

    public boolean createOrUpdate(Restaurant restaurant) {
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
            return true;
        }

        return false;
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