package com.github.Alway09.RestaurantVotingApp.web.restaurant;

import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.service.RestaurantService;
import com.github.Alway09.RestaurantVotingApp.to.CreateRestaurantTo;
import com.github.Alway09.RestaurantVotingApp.to.RestaurantTo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.util.RestaurantUtil.*;
import static com.github.Alway09.RestaurantVotingApp.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.Alway09.RestaurantVotingApp.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantAdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantAdminController {
    public static final String REST_URL = "/api/admin/restaurants";
    private RestaurantService service;

    @Operation(summary = "Get all restaurants")
    @GetMapping("/")
    public List<RestaurantTo> getAll() {
        return createOutcomeTos(service.getAll());
    }

    @Operation(summary = "Get restaurant by id")
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        var restaurant = service.get(id);
        return createOutcomeTo(restaurant);
    }

    @Operation(summary = "Delete restaurant by id", description = "Restaurant menus deletes cascading.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @Operation(summary = "Create restaurant")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody CreateRestaurantTo restaurantTo) {
        log.info("create restaurant {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = service.saveOrUpdate(createFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Update restaurant by id")
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
