package com.sombra.promotion.entity;

import com.sombra.promotion.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

}
