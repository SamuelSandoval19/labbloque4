package com.parque.dinosaurios.modelo;

import com.parque.dinosaurios.modelo.Vehiculo.EstadoVehiculo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoTest {

    @Test
    void vehiculoNuevoEstaDisponible() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        assertTrue(v.estaDisponible());
        assertFalse(v.estaEnUso());
        assertFalse(v.estaAveriado());
        assertTrue(v.estaOperativo());
        assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
    }

    @Test
    void ponerEnUsoCambiaEstado() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        v.ponerEnUso();
        assertTrue(v.estaEnUso());
        assertFalse(v.estaDisponible());
        v.liberar();
        assertTrue(v.estaDisponible());
    }

    @Test
    void averiarPonePasosRestantes() {
        Vehiculo v = new Vehiculo(1, "Jeep", 4);
        v.averiar();
        assertTrue(v.estaAveriado());
        assertFalse(v.estaOperativo());
        assertEquals(4, v.getPasosRestantesReparacion());
    }

    @Test
    void avanzarReparacionDecrementaYRestauraDisponibilidad() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        v.averiar();
        v.avanzarReparacion();
        assertEquals(2, v.getPasosRestantesReparacion());
        assertTrue(v.estaAveriado());
        v.avanzarReparacion();
        v.avanzarReparacion();
        assertEquals(0, v.getPasosRestantesReparacion());
        assertTrue(v.estaDisponible());
    }

    @Test
    void avanzarReparacionSobreVehiculoDisponibleNoHaceNada() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        v.avanzarReparacion();
        assertTrue(v.estaDisponible());
        assertEquals(0, v.getPasosRestantesReparacion());
    }

    @Test
    void usarVehiculoAveriadoLanzaExcepcion() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        v.averiar();
        assertThrows(IllegalStateException.class, v::ponerEnUso);
    }

    @Test
    void usarVehiculoEnUsoLanzaExcepcion() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        v.ponerEnUso();
        assertThrows(IllegalStateException.class, v::ponerEnUso);
    }

    @Test
    void liberarVehiculoDisponibleNoLanza() {
        Vehiculo v = new Vehiculo(1, "Jeep", 3);
        assertDoesNotThrow(v::liberar);
    }

    @Test
    void getteresFuncionan() {
        Vehiculo v = new Vehiculo(3, "Buggy", 5);
        assertEquals(3, v.getIdentificador());
        assertEquals("Buggy", v.getTipo());
        assertTrue(v.toString().contains("Buggy"));
    }
}
