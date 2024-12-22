package com.kennedy.demo_auto_test.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryBuilder {

    public static Example<Planet> makeQuery(Planet planet){
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();

        return Example.of(planet, exampleMatcher);
    }
}
