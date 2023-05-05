package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    @EntityGraph(attributePaths = "dishes")
    @Query("SELECT m FROM Menu m WHERE m.creationDate>=:start_date AND m.creationDate<:end_date")
    List<Menu> getAllBetweenInclusive(@Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);

    @EntityGraph(attributePaths = "dishes")
    List<Menu> findAllById(Iterable<Integer> ids);
}
