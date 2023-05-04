package ru.javaops.topjava2.to;

import lombok.Value;

@Value
public class VoteAmountTo {
    Integer restaurantId;
    Long votesAmount;

    public VoteAmountTo(Integer restaurantId, Long votesAmount) {
        this.restaurantId = restaurantId;
        this.votesAmount = votesAmount;
    }
}
