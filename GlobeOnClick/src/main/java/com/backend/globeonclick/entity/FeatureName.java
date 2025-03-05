package com.backend.globeonclick.entity;

public enum FeatureName {
    TRANSPORTE("Transporte"),
    ALIMENTACION("Alimentación"),
    ALOJAMIENTO("Alojamiento"),
    GUIA_TURISTICO("Guía Turístico"),
    SEGURO_VIAJE("Seguro de Viaje"),
    ACCESIBILIDAD("Accesibilidad"),
    WIFI_GRATIS("WiFi Gratis"),
    TRASLADOS("Traslados"),
    ACTIVIDADES_INCLUIDAS("Actividades Incluidas"),
    EQUIPAJE("Equipaje");

    private final String displayName;

    FeatureName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}