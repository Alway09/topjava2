package ru.javaops.topjava2.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.to.MenuTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNullElse;
import static ru.javaops.topjava2.util.MenuUtil.*;
import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuController {
    MenuRepository repository;
    RestaurantRepository restaurantRepository;

    public static final String REST_URL = "/api/admin/menu";

    @GetMapping("/")
    public List<MenuTo> getAll() {
        log.info("get all menus");
        return createTos(repository.findAll());
    }

    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int id) {
        log.info("get menu id={}", id);
        return createTo(repository.getExisted(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu id={}", id);
        repository.deleteExisted(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody MenuTo menuTo) {
        log.info("update menu id={}", id);
        assureIdConsistent(menuTo, id);
        Menu menu = createFromTo(menuTo);
        Menu actual = repository.getExisted(id);

        menu.setRestaurant(actual.getRestaurant());
        menu.setCreationDate(actual.getCreationDate());
        repository.save(menu);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createWithLocation(@Valid @RequestBody MenuTo menuTo) {
        log.info("create menu {} for restaurant id={}", menuTo, menuTo.getRestaurantId());
        checkNew(menuTo);

        Menu created = repository.save(createFromTo(menuTo));
        created.setCreationDate(requireNonNullElse(menuTo.getCreationDate(), LocalDate.now()));

        Restaurant restaurant = restaurantRepository.getExisted(menuTo.getRestaurantId());
        created.setRestaurant(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }
}
