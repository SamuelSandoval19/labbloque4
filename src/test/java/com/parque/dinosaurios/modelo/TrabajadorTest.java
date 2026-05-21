package com.parque.dinosaurios.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrabajadorTest {

    @Test
    void trabajadorNuevoEstaDisponible() {
        Trabajador t = new Trabajador(1, "Ana", "Cuidador");
        assertTrue(t.estaDisponible());
    }

    @Test
    void asignarTareaLoVuelveNoDisponible() {
        Trabajador t = new Trabajador(2, "Luis", "Guia");
        t.asignarTarea();
        assertFalse(t.estaDisponible());
    }

    @Test
    void liberarVuelveDisponible() {
        Trabajador t = new Trabajador(3, "Maria", "Vigilante");
        t.asignarTarea();
        t.liberar();
        assertTrue(t.estaDisponible());
    }

    @Test
    void getteresFuncionan() {
        Trabajador t = new Trabajador(7, "Pedro", "Mantenimiento");
        assertEquals(7, t.getIdentificador());
        assertEquals("Pedro", t.getNombre());
        assertEquals("Mantenimiento", t.getPuesto());
        assertTrue(t.toString().contains("Pedro"));
    }
}
