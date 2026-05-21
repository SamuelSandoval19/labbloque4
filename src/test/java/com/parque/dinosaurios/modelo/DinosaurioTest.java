package com.parque.dinosaurios.modelo;

import com.parque.dinosaurios.modelo.enumeraciones.TipoDinosaurio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DinosaurioTest {

    private Dinosaurio rex;

    @BeforeEach
    void inicializar() {
        rex = new Dinosaurio(1, "Rex", TipoDinosaurio.CARNIVORO);
    }

    @Test
    void alCrearseEstaEnRecintoYSinHambre() {
        assertTrue(rex.estaEnRecinto());
        assertEquals(0, rex.getNivelHambre());
        assertFalse(rex.estaHambriento());
    }

    @Test
    void aumentarHambreNoSuperaDiez() {
        for (int i = 0; i < 15; i++) {
            rex.aumentarHambre();
        }
        assertEquals(10, rex.getNivelHambre());
    }

    @Test
    void seConsideraHambrientoDesdeNivelSiete() {
        for (int i = 0; i < 7; i++) {
            rex.aumentarHambre();
        }
        assertTrue(rex.estaHambriento());
    }

    @Test
    void alimentarReseteaHambre() {
        for (int i = 0; i < 8; i++) {
            rex.aumentarHambre();
        }
        rex.alimentar();
        assertEquals(0, rex.getNivelHambre());
    }

    @Test
    void escaparYRegresarCambianEstado() {
        rex.marcarComoEscapado();
        assertFalse(rex.estaEnRecinto());
        rex.regresarAlRecinto();
        assertTrue(rex.estaEnRecinto());
    }

    @Test
    void getNombreYTipoSonCorrectos() {
        assertEquals("Rex", rex.getNombre());
        assertEquals(TipoDinosaurio.CARNIVORO, rex.getTipo());
        assertEquals(1, rex.getIdentificador());
    }

    @Test
    void toStringContieneInformacion() {
        String texto = rex.toString();
        assertTrue(texto.contains("Rex"));
        assertTrue(texto.contains("CARNIVORO"));
    }
}
