package com.github.Alway09.RestaurantVotingApp.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateRestaurantTo extends NamedTo {
    public CreateRestaurantTo(Integer id, String name) {
        super(id, name);
    }
}
