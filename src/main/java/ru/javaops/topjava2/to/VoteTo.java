package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo extends BaseTo {
    Integer restaurantId;

    LocalDateTime dateTime;

    public VoteTo(Integer id, Integer restaurantId, LocalDateTime dateTime) {
        super(id);
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }
}
