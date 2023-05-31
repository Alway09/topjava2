package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id AND v.date=:date")
    Optional<Vote> findUserVote(@Param("user_id") int userId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id")
    List<Vote> getAllUserVotes(@Param("user_id") int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id AND v.restaurant.id=:restaurant_id")
    List<Vote> getAllUserVotesForRestaurant(@Param("user_id") int userId, @Param("restaurant_id") int restaurantId);
}
