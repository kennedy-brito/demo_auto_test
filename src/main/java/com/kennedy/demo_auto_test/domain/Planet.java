package com.kennedy.demo_auto_test.domain;

import com.kennedy.demo_auto_test.jacoco.ExcludeFromJacocoGeneratedReport;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String climate;

    @NotEmpty
    @Column(nullable = false)
    private String terrain;

    @ExcludeFromJacocoGeneratedReport
    @Override
    public String toString() {
        return "Planet{" +
                "climate='" + climate + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", terrain='" + terrain + '\'' +
                '}';
    }
}
