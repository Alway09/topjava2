package com.github.Alway09.RestaurantVotingApp.web.menu;

import com.github.Alway09.RestaurantVotingApp.repository.MenuRepository;
import com.github.Alway09.RestaurantVotingApp.to.MenuOutTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.Alway09.RestaurantVotingApp.util.MenuUtil.*;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = MENUS_CACHE_NAME)
public class MenuController {
    public static final String REST_URL = "/api/menus";

    private MenuRepository repository;

    @Operation(summary = "Get menu by id")
    @Cacheable
    @GetMapping("/{id}")
    public MenuOutTo get(@PathVariable int id) {
        log.info("get menu id={}", id);
        return createOutTo(repository.getExisted(id));
    }
}
