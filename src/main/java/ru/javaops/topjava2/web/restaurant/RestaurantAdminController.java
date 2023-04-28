package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.net.URI;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

//TODO: cashing
@RestController
@RequestMapping(value = RestaurantAdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantAdminController extends AbstractRestaurantController {
    public static final String REST_URL = "/api/admin/restaurants";

    @Override
    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        return super.getAllOrByName(name);
    }

    @Override
    @GetMapping("/list")
    public List<Restaurant> getList() {
        return super.getList();
    }

    @Override
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant id={}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createWithLocation(@RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant id={}", id);
        assureIdConsistent(restaurant, id);
        repository.getExisted(id);
        repository.save(restaurant);
    }

    @Override
    @PutMapping("/{restaurantId}/vote")
    public void vote(@PathVariable int restaurantId) {
        super.vote(restaurantId);
    }
}
