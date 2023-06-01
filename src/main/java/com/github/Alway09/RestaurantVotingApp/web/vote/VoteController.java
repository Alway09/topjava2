package com.github.Alway09.RestaurantVotingApp.web.vote;

import com.github.Alway09.RestaurantVotingApp.service.RestaurantService;
import com.github.Alway09.RestaurantVotingApp.service.VoteService;
import com.github.Alway09.RestaurantVotingApp.to.VoteTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.createTo;
import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.createTos;
import static com.github.Alway09.RestaurantVotingApp.AuthUser.authId;
import static com.github.Alway09.RestaurantVotingApp.AuthUser.authUser;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteController {
    public static final String REST_URL = "/api/profile/votes";
    private VoteService service;
    private RestaurantService restaurantService;

    @Operation(summary = "Get all authorized user votes")
    @GetMapping("/")
    public List<VoteTo> getAll() {
        return createTos(service.getAllUserVotes(authId()));
    }

    @Operation(summary = "Get all authorized user votes for restaurant by restaurant id")
    @GetMapping("/{restaurantId}")
    public List<VoteTo> getAllForRestaurant(@PathVariable int restaurantId) {
        restaurantService.findById(restaurantId);
        return createTos(service.getAllUserVotesForRestaurant(restaurantId, authId()));
    }

    @Operation(summary = "Get actual authorized user vote")
    @GetMapping("/actual")
    public VoteTo getActualVote() {
        return createTo(service.getActualVote(authId()));
    }

    @Operation(summary = "Vote for restaurant by restaurant id")
    @PostMapping("/{restaurantId}")
    public VoteTo voteForRestaurant(@PathVariable int restaurantId) {
        return createTo(service.createOrUpdate(restaurantService.findById(restaurantId), authUser()));
    }

    @Operation(summary = "Delete actual authorized user vote")
    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActualVote() {
        service.deleteActualVote(authId());
    }
}
