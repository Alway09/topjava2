package com.github.Alway09.RestaurantVotingApp.repository;

import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT r FROM Restaurant r JOIN r.menus m WHERE r.id=:id AND m.actualDate=:actual_date")
    Optional<Restaurant> getByActualDate(@Param("id") int id, @Param("actual_date") LocalDate actualDate);

    @Query("SELECT r FROM Restaurant r JOIN r.menus m WHERE r.id=m.restaurant.id AND m.actualDate=:actual_date ORDER BY r.name")
    List<Restaurant> getAllByMenuActualDate(@Param("actual_date") LocalDate actualDate);
}

