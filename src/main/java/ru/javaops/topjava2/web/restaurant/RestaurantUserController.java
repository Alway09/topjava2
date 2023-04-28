package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantUserController extends AbstractRestaurantController {
    public static final String REST_URL = "/api/user/restaurants";

    @Override
    @GetMapping(value = "/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        return super.getAllOrByName(name);
    }

    @Override
    @GetMapping("/list")
    public List<Restaurant> getList() {
        return super.getList();
    }

    @Override
    @GetMapping(value = "/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @PutMapping("/{restaurantId}/vote")
    public void vote(@PathVariable int restaurantId) {
        super.vote(restaurantId);
    }
}
