package ru.javaops.topjava2.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RestaurantService {
    RestaurantRepository repository;
    MenuRepository menuRepository;

    public Restaurant get(int id) {
        log.info("get restaurant id={} with menus", id);
        Restaurant restaurant = repository.getWithMenus(id)
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return fetchMenus(restaurant);
    }

    public Restaurant getWithActualMenus(int id) {
        log.info("get restaurant id={} with actual menus", id);
        Restaurant restaurant = repository.getWithMenusCreatedAtTheDate(id, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return fetchMenus(restaurant);
    }

    public Restaurant getByName(String name) {
        log.info("get restaurant by name={}", name);
        return fetchMenus(repository.getByNameWithMenus(name).orElse(null));
    }

    public Restaurant getByNameWithActualMenus(String name) {
        log.info("get restaurant with name={}", name);
        return fetchMenus(repository.getByNameWithMenusCreatedAtTheDate(name, LocalDate.now()).orElse(null));
    }

    public List<Restaurant> getAll() {
        log.info("get all restaurants with menus");
        return fetchMenus(repository.getAllWithMenus());
    }

    public List<Restaurant> getAllWithActualMenus() {
        log.info("get all restaurants with actual menus");
        return fetchMenus(repository.getAllWithMenusCreatedAtDate(LocalDate.now()));
    }

    public List<Restaurant> getList() {
        log.info("get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    public List<Restaurant> getListWithActualMenus() {
        log.info("get all restaurants");
        return repository.getListWithMenusCreatedAtDate(LocalDate.now());
    }

    public Restaurant findById(int id) {
        return repository.getExisted(id);
    }

    public void delete(int id) {
        log.info("delete restaurant id={}", id);
        repository.deleteExisted(id);
    }

    public Restaurant saveOrUpdate(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    private Restaurant fetchMenus(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        List<Menu> fetchedMenus = new ArrayList<>();
        for (Menu menu : restaurant.getMenus()) {
            Menu fetchedMenu = menuRepository.getExisted(menu.id());
            fetchedMenus.add(fetchedMenu);
        }
        restaurant.setMenus(fetchedMenus);
        return restaurant;
    }

    private List<Restaurant> fetchMenus(List<Restaurant> restaurants) {
        List<Restaurant> restaurantsWithFetchedMenus = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantsWithFetchedMenus.add(fetchMenus(restaurant));
        }
        return restaurantsWithFetchedMenus;
    }
}
