package com.dani.couponsystemv2.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class UserEntity extends BaseEntity {

    @NotEmpty
    @Length(min = 5, max = 50)
    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @NotEmpty
    @Length(min = 5)
    @Column(name = "password", nullable = false)
    protected String password;
}
