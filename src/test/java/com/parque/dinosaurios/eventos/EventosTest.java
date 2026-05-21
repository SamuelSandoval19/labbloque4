package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.Dinosaurio;
import com.parque.dinosaurios.modelo.Vehiculo;
import com.parque.dinosaurios.modelo.enumeraciones.TipoDinosaurio;
import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.parque.AyudanteParque;
import com.parque.dinosaurios.parque.Parque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventosTest {

    private Parque parque;

    @BeforeEach
    void inicializar() {
        parque = AyudanteParque.construirParqueLimpio();
    }

    @Test
    void escapeDinosaurioGeneraDescripcionYRegresaAlRecinto() {
        EscapeDinosaurio escape = new EscapeDinosaurio();
        escape.ejecutar(parque);

        assertEquals(TipoEvento.ESCAPE_DINOSAURIO, escape.getTipo());
        assertNotNull(escape.getDescripcion());
        for (Dinosaurio d : parque.getDinosaurios()) {
            assertTrue(d.estaEnRecinto());
        }
    }

    @Test
    void apagonMasivoProvocaFallaYRegistraGasto() {
        var antes = parque.getGastoAcumulado();
        ApagonMasivo apagon = new ApagonMasivo();
        apagon.ejecutar(parque);

        assertEquals(TipoEvento.APAGON_MASIVO, apagon.getTipo());
        assertNotNull(apagon.getDescripcion());
        assertTrue(parque.getGastoAcumulado().compareTo(antes) > 0);
    }

    @Test
    void tormentaTorrencialRefugiaTuristas() {
        // Aseguramos que haya turistas dentro
        for (int i = 0; i < 5; i++) {
            parque.getTuristas().get(i).setEstado(
                    com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista.DENTRO_DEL_PARQUE);
        }
        TormentaTorrencial tormenta = new TormentaTorrencial();
        tormenta.ejecutar(parque);

        assertEquals(TipoEvento.TORMENTA_TORRENCIAL, tormenta.getTipo());
        assertTrue(tormenta.getDescripcion().contains("Tormenta"));
    }

    @Test
    void horaDeOfertasVendeSouvenirsConDescuento() {
        for (int i = 0; i < 5; i++) {
            parque.getTuristas().get(i).setEstado(
                    com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista.DENTRO_DEL_PARQUE);
        }
        var antesIngreso = parque.getIngresoAcumulado();
        HoraOfertas hora = new HoraOfertas();
        hora.ejecutar(parque);

        assertEquals(TipoEvento.HORA_DE_OFERTAS, hora.getTipo());
        assertTrue(parque.getIngresoAcumulado().compareTo(antesIngreso) >= 0);
    }

    @Test
    void fallaVehiculosAveriaYRepara() {
        FallaVehiculos falla = new FallaVehiculos();
        var antesGasto = parque.getGastoAcumulado();
        falla.ejecutar(parque);

        assertEquals(TipoEvento.FALLA_DE_VEHICULOS, falla.getTipo());
        assertNotNull(falla.getDescripcion());
        assertTrue(parque.getGastoAcumulado().compareTo(antesGasto) >= 0);
        for (Vehiculo v : parque.getVehiculos()) {
            assertTrue(v.estaOperativo());
        }
    }

    @Test
    void escapeConTodosLosDinosauriosFueraDevuelveMensaje() {
        for (Dinosaurio d : parque.getDinosaurios()) {
            d.marcarComoEscapado();
        }
        EscapeDinosaurio escape = new EscapeDinosaurio();
        escape.ejecutar(parque);
        assertTrue(escape.getDescripcion().contains("controlados"));
    }

    @Test
    void tiposDeEventoNoSonNulos() {
        for (TipoEvento t : TipoEvento.values()) {
            assertNotNull(t.name());
        }
    }
}
