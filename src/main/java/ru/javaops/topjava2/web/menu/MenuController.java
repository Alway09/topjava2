package ru.javaops.topjava2.web.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.service.RestaurantService;
import ru.javaops.topjava2.to.MenuTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNullElse;
import static ru.javaops.topjava2.util.MenuUtil.*;
import static ru.javaops.topjava2.util.RestaurantUtil.RESTAURANTS_CACHE_NAME;
import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = RESTAURANTS_CACHE_NAME)
public class MenuController {
    MenuRepository repository;
    RestaurantService restaurantService;
    CreationDateValidator creationDateValidator;

    public static final String REST_URL = "/api/admin/menus";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(creationDateValidator);
    }

    @Operation(summary = "Get all menus or menus between dates",
            description = "Get all menus OR get all menus between dates if one or both dates are present.",
            parameters = {
                    @Parameter(name = "startDate", description = "Start date of range"),
                    @Parameter(name = "endDate", description = "End date of range")
            })
    @GetMapping("/")
    public List<MenuTo> getAll(@RequestParam @Nullable LocalDate startDate,
                               @RequestParam @Nullable LocalDate endDate) {
        startDate = requireNonNullElse(startDate, LocalDate.of(2023, 1, 1));
        endDate = requireNonNullElse(endDate, LocalDate.of(3000, 1, 1));
        log.info("get all menus between {} and {}", startDate, endDate);
        return createTos(repository.getAllBetweenInclusive(startDate, endDate));
    }

    @Operation(summary = "Get menu by id")
    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int id) {
        log.info("get menu id={}", id);
        return createTo(repository.getExisted(id));
    }

    @Operation(summary = "Delete menu by id")
    @CacheEvict(allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu id={}", id);
        repository.deleteExisted(id);
    }

    @Operation(summary = "Update menu by id")
    @Caching(evict = {
            @CacheEvict(key = "@menuRepository.getExisted(#id).restaurant.name"),
            @CacheEvict(key = "@menuRepository.getExisted(#id).restaurant.id()")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody MenuTo menuTo) {
        log.info("update menu id={}", id);
        assureIdConsistent(menuTo, id);
        Menu menu = createFromTo(menuTo);
        Menu actual = repository.getExisted(id);

        menu.setRestaurant(actual.getRestaurant());
        menu.setActualDate(actual.getActualDate());
        repository.save(menu);
    }

    @Operation(summary = "Create menu")
    @Caching(evict = {
            @CacheEvict(key = "@restaurantService.findById(#menuTo.restaurantId).name"),
            @CacheEvict(key = "#menuTo.restaurantId")
    })
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menuTo) {
        log.info("create menu {} for restaurant id={}", menuTo, menuTo.getRestaurantId());
        checkNew(menuTo);

        Menu created = createFromTo(menuTo);
        created.setActualDate(requireNonNullElse(menuTo.getActualDate(), LocalDate.now()));

        Restaurant restaurant = restaurantService.findById(menuTo.getRestaurantId());
        created.setRestaurant(restaurant);
        created = repository.save(created);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
