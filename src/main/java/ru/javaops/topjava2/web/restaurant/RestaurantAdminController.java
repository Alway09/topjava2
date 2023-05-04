package ru.javaops.topjava2.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.CreateRestaurantTo;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.util.VoteUtil;

import java.net.URI;
import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.*;
import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

// TODO: cashing
@RestController
@RequestMapping(value = RestaurantAdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantAdminController {
    public static final String REST_URL = "/api/admin/restaurant";
    private RestaurantRepository repository;
    private VoteService voteService;

    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        if (name != null) {
            log.info("get restaurant by name={}", name);
            var restaurant = repository.getByNameWithMenus(name);
            return restaurant
                    .map(dbRestaurant -> List.of(createTo(dbRestaurant, voteService.getVotesAmountBetweenInclusive(dbRestaurant.id(), VoteUtil.votingStart(), VoteUtil.votingEnd()))))
                    .orElseGet(List::of);
        }
        log.info("get all restaurants with menu and votes");
        return createTos(repository.getAllWithMenus(),
                voteService.getVotesAmountOfAllRestaurantsBetweenInclusive(VoteUtil.votingStart(), VoteUtil.votingEnd()));
    }

    @GetMapping("/list")
    public List<Restaurant> getList() {
        log.info("get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        var restaurant = repository.getWithMenus(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return createTo(restaurant, voteService.getVotesAmountBetweenInclusive(id, VoteUtil.votingStart(), VoteUtil.votingEnd()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant id={}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createWithLocation(@Valid @RequestBody CreateRestaurantTo restaurantTo) {
        log.info("create restaurant {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = repository.save(createFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody CreateRestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update restaurant id={}", id);
        assureIdConsistent(restaurantTo, id);
        Restaurant updated = repository.getExisted(id);
        updated.setName(restaurantTo.getName());
        repository.save(updated);
    }
}
