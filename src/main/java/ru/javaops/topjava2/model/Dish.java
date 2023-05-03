package ru.javaops.topjava2.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Dish implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String name;

    @Range
    Integer price;

    public Dish(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
