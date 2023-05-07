package ru.javaops.topjava2.web.vote;

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

@RestController
@RequestMapping(value = VoteProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteProfileController {
    public static final String REST_URL = "/api/profile/votes";
    VoteService service;
    RestaurantService restaurantService;

    @GetMapping("/")
    public List<VoteTo> getAll() {
        return createTos(service.getAllUserVotes());
    }

    @GetMapping("/{restaurantId}")
    public List<VoteTo> getAllForRestaurant(@PathVariable int restaurantId) {
        restaurantService.findById(restaurantId);
        return createTos(service.getAllUserVotesForRestaurant(restaurantId));
    }

    @GetMapping("/actual")
    public VoteTo getActualVote() {
        return createTo(service.getActualVote());
    }

    @PostMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void voteForRestaurant(@PathVariable int restaurantId) {
        service.createOrUpdate(restaurantService.findById(restaurantId));
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActualVote() {
        service.deleteActualVote();
    }
}
