package com.github.Alway09.RestaurantVotingApp.service;

import com.github.Alway09.RestaurantVotingApp.error.NotFoundException;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.repository.MenuRepository;
import com.github.Alway09.RestaurantVotingApp.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.github.Alway09.RestaurantVotingApp.util.RestaurantUtil.RESTAURANTS_CACHE_NAME;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = RESTAURANTS_CACHE_NAME)
public class RestaurantService {
    private RestaurantRepository repository;
    private MenuRepository menuRepository;

    public Restaurant get(int id) {
        log.info("get restaurant id={} with menus", id);
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    @Cacheable
    public Restaurant getWithActualMenus(int id) {
        log.info("get restaurant id={} with actual menus", id);
        return repository.getWithMenusByActualDate(id, LocalDate.now())
                .orElse(null);
    }

    public Restaurant getByName(String name) {
        log.info("get restaurant by name={}", name);
        return repository.getByNameWithMenus(name).orElse(null);
    }

    @Cacheable
    public Restaurant getByNameWithActualMenus(String name) {
        log.info("get restaurant with name={}", name);
        return repository.getByNameWithMenusWithActualDate(name, LocalDate.now()).orElse(null);
    }

    public List<Restaurant> getAll() {
        log.info("get all restaurants with menus");
        return repository.findAll();
    }

    @Cacheable(key = "#root.methodName")
    public List<Restaurant> getAllWithActualMenus() {
        log.info("get all restaurants with actual menus");
        return repository.getAllWithMenusWithByActualDate(LocalDate.now());
    }

    public List<Restaurant> getList() {
        log.info("get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    @Cacheable(key = "#root.methodName")
    public List<Restaurant> getListWithActualMenus() {
        log.info("get all restaurants");
        return repository.getListByActualDate(LocalDate.now());
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
}
