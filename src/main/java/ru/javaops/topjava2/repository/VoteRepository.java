package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.util.VoteUtil;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id AND v.dateTime >=:start_date_time AND v.dateTime <:end_date_time")
    Optional<Vote> findUserVote(@Param("user_id") int userId, @Param("start_date_time") LocalDateTime startDateTime, @Param("end_date_time") LocalDateTime endDateTime);

    @Query("SELECT count(*) FROM Vote v WHERE v.restaurant.id=:restaurant_id AND v.dateTime >=:start_date_time AND v.dateTime <:end_date_time")
    long getVotesBetweenInclusive(@Param("restaurant_id") int restaurantId,
                                  @Param("start_date_time") LocalDateTime startDateTime,
                                  @Param("end_date_time") LocalDateTime endDateTime);

    //key - restaurant_id, value - amount of votes for restaurant with id=restaurant_id
    default Map<Integer, Long> getVotesOfAllRestaurantsBetweenInclusive(LocalDateTime startDateTime,
                                                                        LocalDateTime endDateTime) {
        return findAll().stream()
                .filter(VoteUtil.distinctByKey(vote -> vote.getRestaurant().getId()))
                .collect(Collectors.toMap(vote -> vote.getRestaurant().getId(),
                        vote -> getVotesBetweenInclusive(vote.getRestaurant().getId(), startDateTime, endDateTime)));
    }

    default Map<Integer, Long> getVotesOfAllRestaurants(){
        return getVotesOfAllRestaurantsBetweenInclusive(LocalDateTime.MIN, LocalDateTime.MAX);
    }
}
