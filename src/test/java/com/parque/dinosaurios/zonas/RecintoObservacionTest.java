package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Dinosaurio;
import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoDinosaurio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RecintoObservacionTest {

    private RecintoObservacion recinto;

    @BeforeEach
    void inicializar() {
        recinto = new RecintoObservacion(
                "Carnivoros", 10, new BigDecimal("120.00"), 42L);
    }

    @Test
    void agregarDinosaurioLoIncluye() {
        recinto.agregarDinosaurio(new Dinosaurio(1, "Rex", TipoDinosaurio.CARNIVORO));
        assertEquals(1, recinto.getDinosauriosExhibidos().size());
    }

    @Test
    void cobrarEntradaCobra() {
        Turista t = new Turista(1, "Sofia", new BigDecimal("500.00"));
        assertTrue(recinto.cobrarEntrada(t));
        assertEquals(new BigDecimal("380.00"), t.getDineroDisponible());
    }

    @Test
    void cobrarEntradaSinDineroFalla() {
        Turista t = new Turista(1, "Pobre", new BigDecimal("10.00"));
        assertFalse(recinto.cobrarEntrada(t));
    }

    @Test
    void aplicarEncuestaDevuelveCalificacionEntreUnoYDiez() {
        for (int i = 0; i < 10; i++) {
            int c = recinto.aplicarEncuesta();
            assertTrue(c >= 1 && c <= 10);
        }
        assertEquals(10, recinto.getEncuestasAplicadas());
    }

    @Test
    void promedioEsCero_sinEncuestas() {
        assertEquals(0.0, recinto.obtenerPromedioSatisfaccion());
    }

    @Test
    void promedioCambiaConEncuestas() {
        for (int i = 0; i < 5; i++) {
            recinto.aplicarEncuesta();
        }
        assertTrue(recinto.obtenerPromedioSatisfaccion() > 0);
    }

    @Test
    void getteresFuncionan() {
        assertEquals("Carnivoros", recinto.getTipoExperiencia());
        assertEquals(new BigDecimal("120.00"), recinto.getCostoEntrada());
        assertNotNull(recinto.describirActividad());
        assertEquals("Recinto Carnivoros", recinto.getNombre());
    }
}
