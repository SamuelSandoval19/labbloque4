package com.parque.dinosaurios.eventos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorEventosTest {

    @Test
    void probabilidadCeroNuncaGenera() {
        GeneradorEventos gen = new GeneradorEventos(0.0, 1L);
        for (int i = 0; i < 100; i++) {
            assertFalse(gen.ocurreEventoEnEstePaso());
        }
    }

    @Test
    void probabilidadUnoSiempreGenera() {
        GeneradorEventos gen = new GeneradorEventos(1.0, 1L);
        for (int i = 0; i < 50; i++) {
            assertTrue(gen.ocurreEventoEnEstePaso());
        }
    }

    @Test
    void generarEventoDevuelveAlgunoDeLosCinco() {
        GeneradorEventos gen = new GeneradorEventos(0.5, 1L);
        EventoAleatorio evento = gen.generarEvento();
        assertNotNull(evento);
        assertNotNull(evento.getTipo());
    }

    @Test
    void constructorSinSemillaTambienFunciona() {
        GeneradorEventos gen = new GeneradorEventos(0.5);
        assertNotNull(gen.generarEvento());
    }
}
