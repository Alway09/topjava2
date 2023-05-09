package ru.javaops.topjava2.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.service.RestaurantService;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.CreateRestaurantTo;
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

    @Operation(summary = "Get all restaurants or one restaurant by name",
            description = "Get all restaurants with it's actual menus and actual votes OR get restaurant with it's actual menus and actual votes by name if name is present.",
            parameters = @Parameter(name = "name", description = "Name of the restaurant"))
    @GetMapping("/")
    public List<RestaurantTo> getAllOrByName(@RequestParam @Nullable String name) {
        if (name != null) {
            var restaurant = service.getByNameWithActualMenus(name);
            return restaurant == null ? List.of() : List.of(createTo(restaurant, voteService.getActualVotesAmount(restaurant.id())));
        }
        return createTos(service.getAllWithActualMenus(),
                voteService.getActualVotesAmountOfAllRestaurants());
    }

    @Operation(summary = "Get list of all restaurants with actual menus", description = "Each element contains only name and id.")
    @GetMapping("/list")
    public List<CreateRestaurantTo> getList() {
        return createTos(service.getListWithActualMenus());
    }

    @Operation(summary = "Get restaurant by id", description = "Get restaurant with it's actual menus and actual votes amount by id.")
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        var restaurant = service.getWithActualMenus(id);
        return restaurant != null ? createTo(restaurant, voteService.getActualVotesAmount(id)) : null;
    }
}
