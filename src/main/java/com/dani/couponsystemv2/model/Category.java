package com.dani.couponsystemv2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = true, nullable = false)
    private CategoryType type;

    public Category(CategoryType type) {
        this.type = type;
    }
}
