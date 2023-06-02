package com.github.Alway09.RestaurantVotingApp.web.menu;

import com.github.Alway09.RestaurantVotingApp.model.Menu;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.repository.MenuRepository;
import com.github.Alway09.RestaurantVotingApp.service.RestaurantService;
import com.github.Alway09.RestaurantVotingApp.to.MenuTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.util.MenuUtil.*;
import static com.github.Alway09.RestaurantVotingApp.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.Alway09.RestaurantVotingApp.util.validation.ValidationUtil.checkNew;
import static java.util.Objects.requireNonNullElse;

@RestController
@RequestMapping(value = MenuAdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = MENUS_CACHE_NAME)
public class MenuAdminController {
    private MenuRepository repository;
    private RestaurantService restaurantService;
    private CreationDateValidator creationDateValidator;

    public static final String REST_URL = "/api/admin/menus";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(creationDateValidator);
    }

    @Operation(summary = "Get all menus or menus with actual date, passed as parameter",
            parameters = {
                    @Parameter(name = "actualDate", description = "Actual date of menus")
            })
    @GetMapping("/")
    public List<MenuTo> getAll(@RequestParam @Nullable LocalDate actualDate) {
        if(actualDate == null){
            log.info("get all menus");
            return createTos(repository.findAll(Sort.by(Sort.Direction.DESC, "actualDate")));
        }

        log.info("get all menus with actual date {}", actualDate);
        return createTos(repository.getAllByActualDate(actualDate));
    }

    @Operation(summary = "Delete menu by id")
    @CacheEvict
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu id={}", id);
        repository.deleteExisted(id);
    }

    @Operation(summary = "Update menu by id")
    @PutMapping("/{id}")
    @CacheEvict(key = "#id")
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
