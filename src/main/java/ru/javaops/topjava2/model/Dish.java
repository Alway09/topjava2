package ru.javaops.topjava2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    String name;

    @Min(0)
    Integer price;

    public Dish(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
