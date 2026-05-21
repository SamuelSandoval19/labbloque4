package com.parque.dinosaurios.modelo;

import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TuristaTest {

    private Turista turista;

    @BeforeEach
    void inicializar() {
        turista = new Turista(5, "Juan", new BigDecimal("500.00"));
    }

    @Test
    void estadoInicialEsEnFila() {
        assertEquals(EstadoTurista.EN_FILA, turista.getEstado());
        assertEquals("Fila de entrada", turista.getZonaActual());
    }

    @Test
    void puedePagarSiTieneSuficiente() {
        assertTrue(turista.puedePagar(new BigDecimal("250.00")));
        assertTrue(turista.puedePagar(new BigDecimal("500.00")));
        assertFalse(turista.puedePagar(new BigDecimal("500.01")));
    }

    @Test
    void cobrarReduceElDinero() {
        turista.cobrar(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("400.00"), turista.getDineroDisponible());
    }

    @Test
    void cobrarSinDineroLanzaExcepcion() {
        assertThrows(IllegalStateException.class,
                () -> turista.cobrar(new BigDecimal("9999.00")));
    }

    @Test
    void moverAZonaActualizaUbicacion() {
        turista.moverAZona("Recinto Central");
        assertEquals("Recinto Central", turista.getZonaActual());
    }

    @Test
    void cambiarEstadoFunciona() {
        turista.setEstado(EstadoTurista.DENTRO_DEL_PARQUE);
        assertEquals(EstadoTurista.DENTRO_DEL_PARQUE, turista.getEstado());
    }

    @Test
    void identificadorYNombreSonCorrectos() {
        assertEquals(5, turista.getIdentificador());
        assertEquals("Juan", turista.getNombre());
    }

    @Test
    void toStringContieneNombre() {
        assertTrue(turista.toString().contains("Juan"));
    }
}
