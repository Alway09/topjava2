package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.service.RestaurantService;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.VoteTo;

import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.createTo;
import static ru.javaops.topjava2.util.VoteUtil.createTos;
import static ru.javaops.topjava2.web.AuthUser.authId;
import static ru.javaops.topjava2.web.AuthUser.authUser;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteController {
    public static final String REST_URL = "/api/profile/votes";
    VoteService service;
    RestaurantService restaurantService;

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
