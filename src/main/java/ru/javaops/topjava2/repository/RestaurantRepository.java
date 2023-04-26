package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menu WHERE r.id=:id")
    Optional<Restaurant> getWithMenu(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r WHERE r.name=:name")
    Optional<Restaurant> getByName(@Param("name") String name);
}
