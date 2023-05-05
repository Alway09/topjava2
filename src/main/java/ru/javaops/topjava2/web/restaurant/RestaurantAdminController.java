package ru.javaops.topjava2.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.service.RestaurantService;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.CreateRestaurantTo;
import ru.javaops.topjava2.to.RestaurantTo;

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
    public static final String REST_URL = "/api/admin/restaurants";
    private VoteService voteService;
    private RestaurantService service;

    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        if (name != null) {
            var restaurant = service.getByName(name);
            return restaurant == null ? List.of() : List.of(createTo(restaurant, voteService.getActualVotesAmount(restaurant.id())));
        }
        return createTos(service.getAll(),
                voteService.getActualVotesAmountOfAllRestaurants());
    }

    @GetMapping("/list")
    public List<CreateRestaurantTo> getList() {
        return createTos(service.getList());
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        var restaurant = service.get(id);
        return createTo(restaurant, voteService.getActualVotesAmount(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createWithLocation(@Valid @RequestBody CreateRestaurantTo restaurantTo) {
        log.info("create restaurant {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = service.saveOrUpdate(createFromTo(restaurantTo));
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
        Restaurant updated = service.get(id);
        updated.setName(restaurantTo.getName());
        service.saveOrUpdate(updated);
    }
}
