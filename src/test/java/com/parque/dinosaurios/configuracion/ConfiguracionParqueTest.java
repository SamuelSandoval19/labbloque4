package com.parque.dinosaurios.configuracion;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionParqueTest {

    @Test
    void instanciaEsSingleton() {
        ConfiguracionParque a = ConfiguracionParque.obtenerInstancia();
        ConfiguracionParque b = ConfiguracionParque.obtenerInstancia();
        assertSame(a, b);
    }

    @Test
    void obtenerTextoExistente() {
        String url = ConfiguracionParque.obtenerInstancia().obtenerTexto("db.url");
        assertTrue(url.startsWith("jdbc:postgresql"));
    }

    @Test
    void obtenerEnteroFunciona() {
        int capacidad = ConfiguracionParque.obtenerInstancia()
                .obtenerEntero("parque.capacidad.maxima");
        assertTrue(capacidad > 0);
    }

    @Test
    void obtenerDecimalFunciona() {
        double prob = ConfiguracionParque.obtenerInstancia()
                .obtenerDecimal("parque.simulacion.probabilidad.evento");
        assertTrue(prob >= 0 && prob <= 1);
    }

    @Test
    void obtenerMontoFunciona() {
        BigDecimal precio = ConfiguracionParque.obtenerInstancia()
                .obtenerMonto("parque.precio.boleto");
        assertTrue(precio.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void contieneRetornaTrueParaClavesValidas() {
        assertTrue(ConfiguracionParque.obtenerInstancia().contiene("db.url"));
        assertFalse(ConfiguracionParque.obtenerInstancia().contiene("clave.inexistente"));
    }

    @Test
    void obtenerClaveInexistenteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfiguracionParque.obtenerInstancia().obtenerTexto("no.existe.clave"));
    }
}
