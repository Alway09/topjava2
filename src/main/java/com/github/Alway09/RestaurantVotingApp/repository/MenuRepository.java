package com.github.Alway09.RestaurantVotingApp.repository;

import com.github.Alway09.RestaurantVotingApp.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m FROM Menu m WHERE m.actualDate=:actual_date")
    List<Menu> getAllByActualDate(@Param("actual_date")LocalDate actualDate);

    List<Menu> findAllById(Iterable<Integer> ids);
}
