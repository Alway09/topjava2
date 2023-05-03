package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.List;

@Slf4j
public abstract class AbstractRestaurantController {
    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    protected VoteService voteService;

    public abstract List<RestaurantTo> getAllOrByName(String name);

    public abstract RestaurantTo get(int id);

    public List<Restaurant> getList() {
        log.info("get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    public void vote(int restaurantId) {
        log.info("vote for restaurant id={}", restaurantId);
        voteService.createOrUpdate(repository.getExisted(restaurantId));
    }
}
