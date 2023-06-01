package com.github.Alway09.RestaurantVotingApp.repository;

import com.github.Alway09.RestaurantVotingApp.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    @EntityGraph(attributePaths = "dishes")
    @Query("SELECT m FROM Menu m WHERE m.actualDate>=:start_date AND m.actualDate<:end_date")
    List<Menu> getAllBetweenInclusive(@Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);

    @EntityGraph(attributePaths = "dishes")
    List<Menu> findAllById(Iterable<Integer> ids);
}
