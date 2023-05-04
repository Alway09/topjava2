package ru.javaops.topjava2.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.AuthUser;

import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.createTos;

@RestController
@RequestMapping(value = VoteProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteProfileController {
    public static final String REST_URL = "/api/profile/vote";
    VoteService service;
    RestaurantRepository restaurantRepository;

    @GetMapping("/")
    public List<VoteTo> getAll() {
        return createTos(service.getAllUserVotes());
    }

    @GetMapping("/{restaurantId}")
    public List<VoteTo> getAllForRestaurant(@PathVariable int restaurantId) {
        restaurantRepository.getExisted(restaurantId);
        return createTos(service.getAllUserVotesForRestaurant(restaurantId));
    }

    @PutMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void voteForRestaurant(@PathVariable int restaurantId) {
        service.createOrUpdate(restaurantRepository.getExisted(restaurantId));
    }
}
