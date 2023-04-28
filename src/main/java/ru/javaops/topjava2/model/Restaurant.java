package ru.javaops.topjava2.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.topjava2.HasId;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("name DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Menu> menus;

    public Restaurant(Integer id, String name, List<Menu> menus) {
        super(id, name);
        this.menus = menus;
    }
}
