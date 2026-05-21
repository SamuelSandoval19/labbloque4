package com.parque.dinosaurios.parque;

import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;
import com.parque.dinosaurios.modelo.enumeraciones.TipoIngreso;
import com.parque.dinosaurios.monitoreo.ReporteEstado;
import com.parque.dinosaurios.persistencia.ConexionBaseDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ParqueTest {

    private Parque parque;

    @BeforeEach
    void inicializar() {
        parque = AyudanteParque.construirParqueLimpio();
    }

    @Test
    void parqueSeInicializaConTuristasYDinosaurios() {
        assertFalse(parque.getTuristas().isEmpty());
        assertFalse(parque.getDinosaurios().isEmpty());
        assertFalse(parque.getTrabajadores().isEmpty());
        assertFalse(parque.getVehiculos().isEmpty());
    }

    @Test
    void todasLasZonasFueronCreadas() {
        assertNotNull(parque.getZonaArribo());
        assertNotNull(parque.getRecintoCentral());
        assertNotNull(parque.getZonaSanitarios());
        assertNotNull(parque.getPlantaEnergia());
        assertEquals(2, parque.getRecintosObservacion().size());
    }

    @Test
    void registrarIngresoSumaAcumulado() {
        BigDecimal antes = parque.getIngresoAcumulado();
        parque.registrarIngreso(TipoIngreso.BOLETO, "prueba", new BigDecimal("100.00"));
        assertEquals(antes.add(new BigDecimal("100.00")), parque.getIngresoAcumulado());
    }

    @Test
    void registrarGastoSumaAcumulado() {
        BigDecimal antes = parque.getGastoAcumulado();
        parque.registrarGasto(TipoGasto.MANTENIMIENTO, "prueba", new BigDecimal("80.00"));
        assertEquals(antes.add(new BigDecimal("80.00")), parque.getGastoAcumulado());
    }

    @Test
    void publicarReporteGeneraReporte() {
        ReporteEstado reporte = parque.construirReporte(7);
        assertEquals(7, reporte.getPaso());
        assertEquals(parque.getEventosOcurridos(), reporte.getEventosOcurridos());
    }

    @Test
    void refugiarTuristaEnRecintoCentralLoIngresa() {
        Turista t = parque.getTuristas().get(0);
        t.setEstado(EstadoTurista.DENTRO_DEL_PARQUE);
        assertTrue(parque.refugiarTuristaEnRecintoCentral(t));
    }

    @Test
    void singletonDevuelveLaMismaInstancia() {
        Parque otra = Parque.obtenerInstancia();
        assertSame(parque, otra);
    }

    @Test
    void obtenerSinInicializarLanzaExcepcion() {
        Parque.reiniciarParaPruebas();
        assertThrows(IllegalStateException.class, Parque::obtenerInstancia);
        AyudanteParque.construirParqueLimpio();
    }

    @Test
    void agregarYQuitarObservadorAjustaCantidad() {
        int antes = parque.cantidadObservadores();
        var obs = new com.parque.dinosaurios.monitoreo.MonitorConsola();
        parque.agregarObservador(obs);
        assertEquals(antes + 1, parque.cantidadObservadores());
        parque.quitarObservador(obs);
        assertEquals(antes, parque.cantidadObservadores());
    }

    @Test
    void costoReparacionVehiculoEsPositivo() {
        assertTrue(parque.getCostoReparacionVehiculo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void repositoriosNoSonNulos() {
        assertNotNull(parque.getIngresoRepositorio());
        assertNotNull(parque.getGastoRepositorio());
        assertNotNull(parque.getEventoRepositorio());
    }

    @Test
    void conexionSinInicializarRetornaNoDisponible() {
        ConexionBaseDatos c = new ConexionBaseDatos();
        assertFalse(c.estaDisponible());
    }
}
