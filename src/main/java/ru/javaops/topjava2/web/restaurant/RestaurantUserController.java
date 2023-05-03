package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.util.RestaurantUtil.createTos;

@RestController
@RequestMapping(value = RestaurantUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantUserController extends AbstractRestaurantController {
    public static final String REST_URL = "/api/user/restaurants";

    @Override
    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        if (name != null) {
            log.info("get restaurant with name={}", name);
            var restaurant = repository.getByNameWithMenusUpdatedAtTheDate(name, LocalDate.now());
            return restaurant
                    .map(dbRestaurant -> List.of(createTo(dbRestaurant, voteService.getActualVotes(dbRestaurant.id()))))
                    .orElseGet(List::of);
        }
        log.info("get all restaurants with menus and votes");
        return createTos(repository.getAllWithMenusUpdatedAtDate(LocalDate.now()),
                voteService.getActualVotesOfRestaurants());
    }

    @Override
    @GetMapping("/list")
    public List<Restaurant> getList() {
        return super.getList();
    }

    @Override
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        var restaurant = repository.getWithMenusUpdatedAtTheDate(id, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return createTo(restaurant, voteService.getActualVotes(id));
    }

    @Override
    @PutMapping("/{restaurantId}/vote")
    public void vote(@PathVariable int restaurantId) {
        super.vote(restaurantId);
    }
}
