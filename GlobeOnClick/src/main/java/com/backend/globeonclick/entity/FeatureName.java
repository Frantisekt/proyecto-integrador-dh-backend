package com.backend.globeonclick.entity;

public enum FeatureName {
    ACCESO_A_SPA("Acceso a spa"),
    ACCESO_VIP("Acceso VIP"),
    ACTIVIDADES("Actividades incluidas"),
    ACTIVIDADES_ACUATICAS("Actividades acuáticas"),
    ALOJAMIENTO_1("Hotel"),
    ALOJAMIENTO_2("Hostal"),
    ALOJAMIENTO_3("Bungalow"),
    ALOJAMIENTO_4("Camping"),
    ALOJAMIENTO_5("Hotel 5 Estrellas"),
    ALMUERZO("Almuerzo"),
    AVISTAMIENTO_DE_AVES("Avistamiento de aves"),
    BEBIDAS("Bebidas"),
    BUCEO_CERTIFICADO("Buceo certificado"),
    CAMPING_DE_LUJO("Camping de lujo"),
    CATA_DE_VINOS("Cata de vinos"),
    CENA_ROMANTICA("Cena romántica"),
    CENAS_INCLUIDAS("Cenas incluidas"),
    CLASE_DE_COCINA("Clase de cocina"),
    CLASE_DE_YOGA("Clase de yoga"),
    ENTRADAS_INCLUIDAS("Entradas incluidas"),
    ESQUI_EN_NIEVE("Esquí en nieve"),
    EXPERIENCIA_4X4("Experiencia 4x4"),
    EXPERIENCIA_CULTURAL("Experiencia cultural"),
    EXCURSION_EN_CAMELLO("Excursión en camello"),
    FOTOGRAFIA_PROFESIONAL("Fotografía profesional"),
    GLIDING("Gliding"),
    GUIA_EN_ESPANOL("Guía en español"),
    GUIA_TURISTICO("Guía turístico"),
    MASAJE_RELAJANTE("Masaje relajante"),
    PASEO_EN_GLOBO("Paseo en globo"),
    RENTA_DE_BICIS("Renta de bicis"),
    SAFARI_OPCIONAL("Safari opcional"),
    SEGURO_DE_VIAJE("Seguro de viaje"),
    SHOW_NOCTURNO("Show nocturno"),
    SPA("Spa"),
    TALLER_ARTESANAL("Taller artesanal"),
    TIEMPO_LIBRE("Tiempo libre"),
    TOUR("Tour"),
    TOUR_EN_BARCO("Tour en barco"),
    TOUR_EN_HELICOPTERO("Tour en helicóptero"),
    TRANSPORTE("Transporte"),
    TRANSPORTE_24_7("Transporte 24/7"),
    TRANSPORTE_PRIVADO("Transporte privado"),
    TREKKING_GUIADO("Trekking guiado"),
    VISITA_A_MUSEOS("Visita a museos"),
    VISITAS_ARQUEOLOGICAS("Visitas arqueológicas"),
    VUELOS_INCLUIDOS("Vuelos incluidos"),
    WIFI_GRATUITO("WiFi gratuito");

    private final String displayName;

    FeatureName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}