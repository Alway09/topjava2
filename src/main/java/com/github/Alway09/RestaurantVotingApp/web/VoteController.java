package com.github.Alway09.RestaurantVotingApp.web;

import com.github.Alway09.RestaurantVotingApp.service.RestaurantService;
import com.github.Alway09.RestaurantVotingApp.service.VoteService;
import com.github.Alway09.RestaurantVotingApp.to.VoteTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.AuthUser.authId;
import static com.github.Alway09.RestaurantVotingApp.AuthUser.authUser;
import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.createTo;
import static com.github.Alway09.RestaurantVotingApp.util.VoteUtil.createTos;

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
    @GetMapping("/restaurant")
    public List<VoteTo> getAllForRestaurant(@RequestParam int restaurantId) {
        restaurantService.findById(restaurantId);
        return createTos(service.getAllUserVotesForRestaurant(restaurantId, authId()));
    }

    @Operation(summary = "Get actual authorized user vote")
    @GetMapping("/actual")
    public VoteTo getActualVote() {
        return createTo(service.getActualVote(authId()));
    }

    @Operation(summary = "Vote for restaurant")
    @PostMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteTo voteForRestaurant(@RequestBody int restaurantId) {
        return createTo(service.create(restaurantService.findById(restaurantId), authUser()));
    }

    @Operation(summary = "Updating actual user vote")
    @PutMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteTo updateVote(@RequestBody int restaurantId) {
        return createTo(service.update(restaurantService.findById(restaurantId), authId()));
    }
}
