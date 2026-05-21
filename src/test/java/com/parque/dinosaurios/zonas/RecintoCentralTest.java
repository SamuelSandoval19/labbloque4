package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Turista;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RecintoCentralTest {

    @Test
    void venderSouvenirCobraYContabiliza() {
        RecintoCentral rc = new RecintoCentral(10, new BigDecimal("180.00"));
        Turista t = new Turista(1, "Lupita", new BigDecimal("500.00"));

        assertTrue(rc.venderSouvenir(t));
        assertEquals(1, rc.getSouvenirsVendidos());
        assertEquals(new BigDecimal("320.00"), t.getDineroDisponible());
    }

    @Test
    void venderSouvenirSinDineroFalla() {
        RecintoCentral rc = new RecintoCentral(10, new BigDecimal("180.00"));
        Turista t = new Turista(1, "Pobre", new BigDecimal("10.00"));
        assertFalse(rc.venderSouvenir(t));
        assertEquals(0, rc.getSouvenirsVendidos());
    }

    @Test
    void consultarInformacionResponde() {
        RecintoCentral rc = new RecintoCentral(10, new BigDecimal("180.00"));
        assertTrue(rc.consultarInformacion("HORARIO").contains("19:00"));
        assertTrue(rc.consultarInformacion("mapa").contains("Recinto"));
        assertTrue(rc.consultarInformacion("seguridad").contains("barreras"));
        assertTrue(rc.consultarInformacion("otro").contains("trabajador"));
    }

    @Test
    void describirActividadEntregaTexto() {
        RecintoCentral rc = new RecintoCentral(10, new BigDecimal("180.00"));
        assertNotNull(rc.describirActividad());
        assertEquals(new BigDecimal("180.00"), rc.getPrecioSouvenir());
    }
}
