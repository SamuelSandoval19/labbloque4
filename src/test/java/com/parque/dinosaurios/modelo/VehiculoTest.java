package com.parque.dinosaurios.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoTest {

    @Test
    void vehiculoNuevoOperativoYNoEnUso() {
        Vehiculo v = new Vehiculo(1, "Jeep");
        assertTrue(v.estaOperativo());
        assertFalse(v.estaEnUso());
    }

    @Test
    void ponerEnUsoLoActiva() {
        Vehiculo v = new Vehiculo(1, "Jeep");
        v.ponerEnUso();
        assertTrue(v.estaEnUso());
        v.liberar();
        assertFalse(v.estaEnUso());
    }

    @Test
    void averiarLoSacaDeOperacion() {
        Vehiculo v = new Vehiculo(1, "Jeep");
        v.ponerEnUso();
        v.averiar();
        assertFalse(v.estaOperativo());
        assertFalse(v.estaEnUso());
    }

    @Test
    void usarVehiculoAveriadoLanzaExcepcion() {
        Vehiculo v = new Vehiculo(1, "Jeep");
        v.averiar();
        assertThrows(IllegalStateException.class, v::ponerEnUso);
    }

    @Test
    void repararLoDejaOperativoOtraVez() {
        Vehiculo v = new Vehiculo(1, "Jeep");
        v.averiar();
        v.reparar();
        assertTrue(v.estaOperativo());
    }

    @Test
    void getteresFuncionan() {
        Vehiculo v = new Vehiculo(3, "Buggy");
        assertEquals(3, v.getIdentificador());
        assertEquals("Buggy", v.getTipo());
        assertTrue(v.toString().contains("Buggy"));
    }
}
