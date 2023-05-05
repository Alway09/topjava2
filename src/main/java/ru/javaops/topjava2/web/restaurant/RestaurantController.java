package ru.javaops.topjava2.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.service.RestaurantService;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.util.RestaurantUtil.createTos;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {
    public static final String REST_URL = "/api/restaurants";
    RestaurantService service;
    private VoteService voteService;

    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        if (name != null) {
            var restaurant = service.getByNameWithActualMenus(name);
            return restaurant == null ? List.of() : List.of(createTo(restaurant, voteService.getActualVotesAmount(restaurant.id())));
        }
        return createTos(service.getAllWithActualMenus(),
                voteService.getActualVotesAmountOfAllRestaurants());
    }

    @GetMapping("/list")
    public List<Restaurant> getList() {
        return service.getListWithActualMenus();
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        var restaurant = service.getWithActualMenus(id);
        return createTo(restaurant, voteService.getActualVotesAmount(id));
    }
}
