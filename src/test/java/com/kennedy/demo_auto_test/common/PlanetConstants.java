package com.kennedy.demo_auto_test.common;

import com.kennedy.demo_auto_test.domain.Planet;

public class PlanetConstants {

    public static final Planet EMPTY_PLANET = new Planet();
    public static final Planet PLANET = new Planet(null, "name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet(null, "", "", "");
}
