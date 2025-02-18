package com.github.Alway09.RestaurantVotingApp.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode
public class VoteTo {
    Integer restaurantId;

    LocalDate date;

    public VoteTo(Integer restaurantId, LocalDate date) {
        this.restaurantId = restaurantId;
        this.date = date;
    }
}
