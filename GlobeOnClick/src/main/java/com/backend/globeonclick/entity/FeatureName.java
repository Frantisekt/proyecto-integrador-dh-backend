package com.backend.globeonclick.entity;

public enum FeatureName {
    TRANSPORTE("Transporte"),
    ALMUERZO("Almuerzo"),
    BEBIDAS("Bebidas"),
    ALOJAMIENTO_1("Alojamiento"),
    ALOJAMIENTO_2("Alojamiento"),
    ALOJAMIENTO_3("Alojamiento"),
    ALOJAMIENTO_4("Alojamiento"),
    ALOJAMIENTO_5("Alojamiento"),
    TOUR("Tour"),
    SPA("Spa"),
    GLIDING("Gliding"),
    ACTIVIDADES("Actividades incluidas");

    private final String displayName;

    FeatureName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}