package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode
public class VoteTo {
    Integer restaurantId;

    LocalDateTime dateTime;

    public VoteTo(Integer restaurantId, LocalDateTime dateTime) {
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }
}
