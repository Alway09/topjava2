package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.javaops.topjava2.HasId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "menu_id"})})
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Range
    Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Menu menu;

    public Dish(Integer id, String name, Menu menu, Integer price) {
        super(id, name);
        this.menu = menu;
        this.price = price;
    }
}
