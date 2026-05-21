package com.parque.dinosaurios.persistencia;

import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;
import com.parque.dinosaurios.modelo.enumeraciones.TipoIngreso;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepositoriosTest {

    @Test
    void ingresoRepositorioConBDNoDisponibleNoLanza() {
        ConexionBaseDatos conexion = mock(ConexionBaseDatos.class);
        when(conexion.estaDisponible()).thenReturn(false);

        IngresoRepositorio repo = new IngresoRepositorio(conexion);
        assertDoesNotThrow(() ->
                repo.guardar(TipoIngreso.BOLETO, "test", new BigDecimal("100")));
        assertEquals(BigDecimal.ZERO, repo.sumarTotal());
        assertTrue(repo.listar().isEmpty());
    }

    @Test
    void gastoRepositorioConBDNoDisponibleNoLanza() {
        ConexionBaseDatos conexion = mock(ConexionBaseDatos.class);
        when(conexion.estaDisponible()).thenReturn(false);

        GastoRepositorio repo = new GastoRepositorio(conexion);
        assertDoesNotThrow(() ->
                repo.guardar(TipoGasto.MANTENIMIENTO, "test", new BigDecimal("50")));
        assertEquals(BigDecimal.ZERO, repo.sumarTotal());
        assertTrue(repo.listar().isEmpty());
    }

    @Test
    void eventoRepositorioConBDNoDisponibleNoLanza() {
        ConexionBaseDatos conexion = mock(ConexionBaseDatos.class);
        when(conexion.estaDisponible()).thenReturn(false);

        EventoRepositorio repo = new EventoRepositorio(conexion);
        assertDoesNotThrow(() ->
                repo.guardar(TipoEvento.APAGON_MASIVO, "test", 1, true));
        assertTrue(repo.listar().isEmpty());
        assertTrue(repo.contarPorTipo().isEmpty());
    }

    @Test
    void conexionFalsaIntentaConectarYFalla() {
        ConexionBaseDatos c = new ConexionBaseDatos(
                "jdbc:postgresql://localhost:1/no-existe",
                "x", "x", "org.postgresql.Driver");
        assertFalse(c.intentarConectar());
        assertFalse(c.estaDisponible());
    }

    @Test
    void ejecutarMigracionesSinDisponibilidadNoHaceNada() {
        ConexionBaseDatos c = new ConexionBaseDatos();
        assertDoesNotThrow(c::ejecutarMigraciones);
    }

    @Test
    void recordsIngresoYGastoConservanDatos() {
        var ingreso = new IngresoRepositorio.RegistroIngreso(
                1L, "BOLETO", "x", new BigDecimal("10"), java.time.LocalDateTime.now());
        assertEquals(1L, ingreso.id());

        var gasto = new GastoRepositorio.RegistroGasto(
                2L, "MANTENIMIENTO", "y", new BigDecimal("20"), java.time.LocalDateTime.now());
        assertEquals(2L, gasto.id());

        var evento = new EventoRepositorio.RegistroEvento(
                3L, "APAGON_MASIVO", "z", 5, true, java.time.LocalDateTime.now());
        assertEquals(5, evento.pasoSimulacion());

        var conteo = new EventoRepositorio.ConteoEvento("APAGON_MASIVO", 7);
        assertEquals(7, conteo.cantidad());
    }
}
