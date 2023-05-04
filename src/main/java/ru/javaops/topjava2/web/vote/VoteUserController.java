package ru.javaops.topjava2.web.vote;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.VoteAmountTo;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.createTos;

@RestController
@RequestMapping(value = VoteUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteUserController {
    public static final String REST_URL = "/api/user/vote";

    VoteService service;
    RestaurantRepository restaurantRepository;

    @GetMapping("/")
    public List<VoteAmountTo> getAllVotesAmount(@RequestParam @Nullable LocalDateTime startDateTime,
                                                @RequestParam @Nullable LocalDateTime endDateTime) {
        return createTos(service.getVotesAmountOfAllRestaurantsBetweenInclusive(startDateTime, endDateTime));
    }

    @GetMapping("/{restaurantId}")
    public Long getVotesAmount(@PathVariable int restaurantId,
                               @RequestParam @Nullable LocalDateTime startDateTime,
                               @RequestParam @Nullable LocalDateTime endDateTime) {
        restaurantRepository.getExisted(restaurantId);
        return service.getVotesAmountBetweenInclusive(restaurantId, startDateTime, endDateTime);
    }
}