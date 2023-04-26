package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dishes WHERE m.id=:id")
    Optional<Menu> getWithDishes(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.lastUpdate>=:start_date_time AND m.lastUpdate<:end_date_time")
    List<Menu> getAllBetweenInclusive(@Param("start_date_time") LocalDateTime startDateTime, @Param("end_date_time") LocalDateTime endDateTime);
}
