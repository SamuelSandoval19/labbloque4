package com.parque.dinosaurios.modelo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BoletoTest {

    @Test
    void boletoConservaLosDatos() {
        Boleto b = new Boleto(100, 5, new BigDecimal("250.00"));
        assertEquals(100, b.getFolio());
        assertEquals(5, b.getIdentificadorTurista());
        assertEquals(new BigDecimal("250.00"), b.getPrecio());
        assertNotNull(b.getFechaEmision());
        assertTrue(b.toString().contains("100"));
    }
}
