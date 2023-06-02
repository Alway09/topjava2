package com.github.Alway09.RestaurantVotingApp.service;

import com.github.Alway09.RestaurantVotingApp.error.NotFoundException;
import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import com.github.Alway09.RestaurantVotingApp.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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

    public Restaurant get(int id) {
        log.info("get restaurant id={}", id);
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }

    @Cacheable
    public Restaurant getWithActualMenus(int id) {
        log.info("get restaurant id={} with actual menus", id);
        return repository.getByActualDate(id, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }

    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    @Cacheable(key = "#root.methodName")
    public List<Restaurant> getAllWithActualMenus() {
        log.info("get all restaurants");
        return repository.getListByActualDate(LocalDate.now());
    }

    public Restaurant findById(int id) {
        return repository.getExisted(id);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        log.info("delete restaurant id={}", id);
        repository.deleteExisted(id);
    }

    @CacheEvict(allEntries = true)
    public Restaurant saveOrUpdate(Restaurant restaurant) {
        return repository.save(restaurant);
    }
}
