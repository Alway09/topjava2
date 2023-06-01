package com.github.Alway09.RestaurantVotingApp.repository;

import com.github.Alway09.RestaurantVotingApp.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id AND v.date=:date")
    Optional<Vote> findUserVote(@Param("user_id") int userId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id ORDER BY v.date DESC")
    List<Vote> getAllUserVotes(@Param("user_id") int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id AND v.restaurant.id=:restaurant_id")
    List<Vote> getAllUserVotesForRestaurant(@Param("user_id") int userId, @Param("restaurant_id") int restaurantId);
}
