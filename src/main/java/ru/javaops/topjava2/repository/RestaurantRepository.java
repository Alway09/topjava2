package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @EntityGraph(attributePaths = "menus")
    Optional<Restaurant> findById(int id);

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r JOIN FETCH Menu m ON r.id=m.restaurant.id WHERE r.id=:id AND m.actualDate=:actual_date")
    Optional<Restaurant> getWithMenusWithActualDate(@Param("id") int id, @Param("actual_date") LocalDate actualDate);

    @Override
    @EntityGraph(attributePaths = "menus")
    List<Restaurant> findAll();

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r JOIN FETCH Menu m ON r.id=m.restaurant.id WHERE m.actualDate=:actual_date")
    List<Restaurant> getAllWithMenusWithActualDate(@Param("actual_date") LocalDate actualDate);

    @Query("SELECT r FROM Restaurant r JOIN FETCH Menu m ON r.id=m.restaurant.id WHERE m.actualDate=:actual_date ORDER BY r.name")
    List<Restaurant> getListWithMenusWithActualDate(@Param("actual_date") LocalDate actualDate);

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r WHERE r.name=:name")
    Optional<Restaurant> getByNameWithMenus(@Param("name") String name);

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r JOIN FETCH Menu m ON r.id=m.restaurant.id WHERE r.name=:name AND m.actualDate=:actual_date")
    Optional<Restaurant> getByNameWithMenusWithActualDate(@Param("name") String name, @Param("actual_date") LocalDate actualDate);
}

