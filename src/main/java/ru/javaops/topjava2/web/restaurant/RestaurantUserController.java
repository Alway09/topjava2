package ru.javaops.topjava2.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.util.VoteUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.util.RestaurantUtil.createTos;

@RestController
@RequestMapping(value = RestaurantUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantUserController {
    public static final String REST_URL = "/api/user/restaurant";
    private RestaurantRepository repository;
    private VoteService voteService;

    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        if (name != null) {
            log.info("get restaurant with name={}", name);
            var restaurant = repository.getByNameWithMenusCreatedAtTheDate(name, LocalDate.now());
            return restaurant
                    .map(dbRestaurant -> List.of(createTo(dbRestaurant, voteService.getVotesAmountBetweenInclusive(dbRestaurant.id(), VoteUtil.votingStart(), VoteUtil.votingEnd()))))
                    .orElseGet(List::of);
        }
        log.info("get all restaurants with menus and votes");
        return createTos(repository.getAllWithMenusCreatedAtDate(LocalDate.now()),
                voteService.getVotesAmountOfAllRestaurantsBetweenInclusive(VoteUtil.votingStart(), VoteUtil.votingEnd()));
    }

    @GetMapping("/list")
    public List<Restaurant> getList() {
        log.info("get all restaurants");
        return repository.getListWithMenusCreatedAtDate(LocalDate.now());
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        var restaurant = repository.getWithMenusCreatedAtTheDate(id, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return createTo(restaurant, voteService.getVotesAmountBetweenInclusive(id, VoteUtil.votingStart(), VoteUtil.votingEnd()));
    }
}
