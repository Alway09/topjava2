package com.github.Alway09.RestaurantVotingApp.repository;

import com.github.Alway09.RestaurantVotingApp.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @EntityGraph(attributePaths = "menus")
    Optional<Restaurant> findById(int id);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE r.id=:id AND m.actualDate=:actual_date")
    Optional<Restaurant> getWithMenusByActualDate(@Param("id") int id, @Param("actual_date") LocalDate actualDate);

    @Override
    @EntityGraph(attributePaths = "menus")
    List<Restaurant> findAll();

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE m.actualDate=:actual_date")
    List<Restaurant> getAllWithMenusWithByActualDate(@Param("actual_date") LocalDate actualDate);

    @Query("SELECT r FROM Restaurant r JOIN r.menus m WHERE r.id=m.restaurant.id AND m.actualDate=:actual_date ORDER BY r.name")
    List<Restaurant> getListByActualDate(@Param("actual_date") LocalDate actualDate);

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r WHERE r.name=:name")
    Optional<Restaurant> getByNameWithMenus(@Param("name") String name);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE r.id=m.restaurant.id AND r.name=:name AND m.actualDate=:actual_date")
    Optional<Restaurant> getByNameWithMenusWithActualDate(@Param("name") String name, @Param("actual_date") LocalDate actualDate);
}

