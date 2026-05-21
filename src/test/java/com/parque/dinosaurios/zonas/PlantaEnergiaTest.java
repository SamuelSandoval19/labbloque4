package com.parque.dinosaurios.zonas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlantaEnergiaTest {

    private PlantaEnergia planta;

    @BeforeEach
    void inicializar() {
        planta = new PlantaEnergia(
                1000, 25, 100,
                new BigDecimal("2.50"),
                new BigDecimal("500.00"));
    }

    @Test
    void valoresInicialesCorrectos() {
        assertEquals(1000, planta.getEnergiaDisponible());
        assertEquals(1000, planta.getEnergiaMaxima());
        assertFalse(planta.estaEnFalla());
        assertEquals(100, planta.getPorcentajeCarga());
        assertTrue(planta.tieneEnergiaSuficiente());
    }

    @Test
    void consumirEnergiaReduceCargaYDevuelveCosto() {
        BigDecimal costo = planta.consumirEnergia();
        assertEquals(975, planta.getEnergiaDisponible());
        assertEquals(new BigDecimal("62.50"), costo);
    }

    @Test
    void enFallaNoConsume() {
        planta.provocarFalla();
        BigDecimal costo = planta.consumirEnergia();
        assertEquals(BigDecimal.ZERO, costo);
        assertFalse(planta.tieneEnergiaSuficiente());
    }

    @Test
    void mantenimientoRestauraCargaYDevuelveCosto() {
        for (int i = 0; i < 30; i++) {
            planta.consumirEnergia();
        }
        BigDecimal costo = planta.realizarMantenimiento();
        assertEquals(1000, planta.getEnergiaDisponible());
        assertEquals(new BigDecimal("500.00"), costo);
        assertFalse(planta.estaEnFalla());
    }

    @Test
    void porcentajeCargaCalculaCorrecto() {
        for (int i = 0; i < 20; i++) {
            planta.consumirEnergia();
        }
        assertEquals(50, planta.getPorcentajeCarga());
    }

    @Test
    void describirActividadEntregaTexto() {
        assertNotNull(planta.describirActividad());
    }
}
