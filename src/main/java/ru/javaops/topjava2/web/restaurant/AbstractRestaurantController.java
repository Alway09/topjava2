package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.util.RestaurantUtil.createTos;

@Slf4j
public abstract class AbstractRestaurantController {
    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    protected VoteService voteService;

    public List<RestaurantTo> getAllOrByName(String name) {
        if (name != null) {
            log.info("get restaurant with name={}", name);
            var restaurant = repository.getByNameWithMenus(name);
            return restaurant
                    .map(dbRestaurant -> List.of(createTo(dbRestaurant, voteService.getActualVotes(dbRestaurant.id()))))
                    .orElseGet(List::of);
        }
        log.info("get all restaurantTos");
        return createTos(repository.getAllWithMenus(),
                voteService.getActualVotesOfRestaurants());
    }

    public List<Restaurant> getList() {
        log.info("get all restaurant");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    public RestaurantTo get(int id) {
        log.info("get restaurant id={}", id);
        var restaurant = repository.getWithMenus(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return createTo(restaurant, voteService.getActualVotes(id));
    }

    public void vote(int restaurantId) {
        log.info("vote for restaurant id={}", restaurantId);
        voteService.createOrUpdate(repository.getExisted(restaurantId));
    }
}
