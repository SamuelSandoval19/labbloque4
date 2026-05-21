package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Turista;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ZonaSanitariosTest {

    @Test
    void ingresarTuristaAsignaTiempo() {
        ZonaSanitarios b = new ZonaSanitarios(5, new BigDecimal("350.00"));
        Turista t = new Turista(1, "Mario", new BigDecimal("1000.00"));
        assertTrue(b.ingresarTurista(t));
        assertEquals(1, b.getOcupacion());
        assertFalse(b.turistaTerminoUso(t.getIdentificador()));
    }

    @Test
    void avanzarTiempoMarcaCuandoTermina() {
        ZonaSanitarios b = new ZonaSanitarios(5, new BigDecimal("350.00"));
        Turista t = new Turista(1, "Mario", new BigDecimal("1000.00"));
        b.ingresarTurista(t);
        b.avanzarTiempo();
        b.avanzarTiempo();
        assertTrue(b.turistaTerminoUso(t.getIdentificador()));
    }

    @Test
    void retirarTuristaLiberaEspacio() {
        ZonaSanitarios b = new ZonaSanitarios(5, new BigDecimal("350.00"));
        Turista t = new Turista(1, "Mario", new BigDecimal("1000.00"));
        b.ingresarTurista(t);
        b.retirarTurista(t);
        assertEquals(0, b.getOcupacion());
    }

    @Test
    void contratarSpaCobraYCuenta() {
        ZonaSanitarios b = new ZonaSanitarios(5, new BigDecimal("350.00"));
        Turista t = new Turista(1, "Mario", new BigDecimal("1000.00"));
        assertTrue(b.contratarSpa(t));
        assertEquals(1, b.getServiciosSpaVendidos());
        assertEquals(new BigDecimal("650.00"), t.getDineroDisponible());
    }

    @Test
    void contratarSpaSinDineroFalla() {
        ZonaSanitarios b = new ZonaSanitarios(5, new BigDecimal("350.00"));
        Turista t = new Turista(1, "Pobre", new BigDecimal("10.00"));
        assertFalse(b.contratarSpa(t));
    }

    @Test
    void describirActividadEntregaTexto() {
        ZonaSanitarios b = new ZonaSanitarios(5, new BigDecimal("350.00"));
        assertNotNull(b.describirActividad());
        assertEquals(new BigDecimal("350.00"), b.getPrecioSpa());
    }
}
