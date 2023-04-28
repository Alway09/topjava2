package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Optional<Restaurant> getWithMenus(@Param("id") int id);

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r ORDER BY r.name DESC")
    List<Restaurant> getAllWithMenus();

    @EntityGraph(attributePaths = "menus")
    @Query("SELECT r FROM Restaurant r WHERE r.name=:name")
    Optional<Restaurant> getByNameWithMenus(@Param("name") String name);
}
