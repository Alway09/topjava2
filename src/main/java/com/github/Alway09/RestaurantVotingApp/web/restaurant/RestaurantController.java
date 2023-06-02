package com.github.Alway09.RestaurantVotingApp.web.restaurant;

import com.github.Alway09.RestaurantVotingApp.service.RestaurantService;
import com.github.Alway09.RestaurantVotingApp.to.RestaurantTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.util.RestaurantUtil.createTos;
import static com.github.Alway09.RestaurantVotingApp.util.RestaurantUtil.createTo;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {
    public static final String REST_URL = "/api/restaurants";
    private RestaurantService service;

    @Operation(summary = "Get all restaurants that have actual menus")
    @GetMapping("/")
    public List<RestaurantTo> getAll() {
        return createTos(service.getAllWithActualMenus());
    }

    @Operation(summary = "Get restaurant that have actual menu by id")
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        return createTo(service.getWithActualMenus(id));
    }
}
