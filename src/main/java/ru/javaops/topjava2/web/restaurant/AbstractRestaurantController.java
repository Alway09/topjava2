package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;

@Slf4j
public abstract class AbstractRestaurantController {
    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    protected VoteService voteService;

    public void vote(int restaurantId) {
        log.info("vote for restaurant id={}", restaurantId);
        voteService.createOrUpdate(repository.getExisted(restaurantId));
    }
}
