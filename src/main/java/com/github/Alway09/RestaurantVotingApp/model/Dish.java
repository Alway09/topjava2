package com.github.Alway09.RestaurantVotingApp.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Dish {
    @NotBlank
    private String name;

    @Min(0)
    private Integer price;

    public Dish(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
