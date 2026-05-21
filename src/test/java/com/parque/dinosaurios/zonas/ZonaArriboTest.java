package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Boleto;
import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ZonaArriboTest {

    private ZonaArribo zona;

    @BeforeEach
    void inicializar() {
        zona = new ZonaArribo(3, new BigDecimal("250.00"));
    }

    @Test
    void zonaNuevaSinTuristasEnFila() {
        assertEquals(0, zona.getTuristasEnFila());
        assertEquals(0, zona.getOcupacion());
        assertTrue(zona.tieneEspacio());
    }

    @Test
    void venderBoletoExitosoCobraYRegistra() {
        Turista t = new Turista(1, "Ana", new BigDecimal("500.00"));
        Boleto boleto = zona.venderBoleto(t);

        assertNotNull(boleto);
        assertEquals(new BigDecimal("250.00"), t.getDineroDisponible());
        assertEquals(1, zona.getBoletosVendidos().size());
    }

    @Test
    void venderBoletoSinDineroDevuelveNull() {
        Turista t = new Turista(1, "Pobre", new BigDecimal("10.00"));
        assertNull(zona.venderBoleto(t));
    }

    @Test
    void atenderFilaVendeYDejaDentro() {
        Turista t = new Turista(1, "Carlos", new BigDecimal("500.00"));
        zona.agregarAFila(t);

        Turista atendido = zona.atenderSiguienteDeFila();
        assertSame(t, atendido);
        assertEquals(EstadoTurista.DENTRO_DEL_PARQUE, t.getEstado());
        assertEquals(1, zona.getOcupacion());
    }

    @Test
    void atenderFilaSinTuristasDevuelveNull() {
        assertNull(zona.atenderSiguienteDeFila());
    }

    @Test
    void registrarSalidaLiberaEspacio() {
        Turista t = new Turista(1, "Carlos", new BigDecimal("500.00"));
        zona.agregarAFila(t);
        zona.atenderSiguienteDeFila();

        zona.registrarSalida(t);
        assertEquals(0, zona.getOcupacion());
        assertEquals(EstadoTurista.FUERA_DEL_PARQUE, t.getEstado());
    }

    @Test
    void capacidadMaximaNoSeExcede() {
        for (int i = 1; i <= 5; i++) {
            zona.agregarAFila(new Turista(i, "T" + i, new BigDecimal("500.00")));
        }
        int ingresados = 0;
        while (zona.atenderSiguienteDeFila() != null) {
            ingresados++;
        }
        assertEquals(3, ingresados);
        assertEquals(3, zona.getOcupacion());
    }

    @Test
    void describirActividadEntregaTexto() {
        assertNotNull(zona.describirActividad());
        assertEquals("Zona de Arribo", zona.getNombre());
        assertEquals(3, zona.getCapacidadMaxima());
        assertEquals(new BigDecimal("250.00"), zona.getPrecioBoleto());
    }
}
