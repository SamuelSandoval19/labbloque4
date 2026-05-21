package com.parque.dinosaurios.simulacion;

import com.parque.dinosaurios.parque.AyudanteParque;
import com.parque.dinosaurios.parque.Parque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MotorSimulacionTest {

    private Parque parque;

    @BeforeEach
    void inicializar() {
        parque = AyudanteParque.construirParqueLimpio();
    }

    @Test
    void ejecutarAvanzaPasos() {
        MotorSimulacion motor = new MotorSimulacion(parque, new Random(1L));
        motor.ejecutar();
        assertTrue(motor.getPasoActual() > 0);
    }

    @Test
    void detenerCorta() {
        MotorSimulacion motor = new MotorSimulacion(parque, new Random(1L));
        motor.detener();
        motor.ejecutar();
        assertEquals(0, motor.getPasoActual());
    }

    @Test
    void unPasoNoLanzaExcepcion() {
        MotorSimulacion motor = new MotorSimulacion(parque, new Random(1L));
        assertDoesNotThrow(motor::ejecutarPaso);
    }

    @Test
    void ejecutarRegistraIngresosOEventos() {
        MotorSimulacion motor = new MotorSimulacion(parque, new Random(42L));
        motor.ejecutar();
        assertTrue(parque.getIngresoAcumulado().signum() >= 0);
        assertTrue(parque.getGastoAcumulado().signum() >= 0);
    }
}
