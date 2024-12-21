package com.kennedy.demo_auto_test.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@Entity
@Table(name = "planets")
public class Planet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String climate;
    private String terrain;
}
