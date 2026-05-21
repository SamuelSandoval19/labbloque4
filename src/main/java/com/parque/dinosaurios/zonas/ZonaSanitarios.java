package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Turista;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ZonaSanitarios extends Zona {

    private final BigDecimal precioSpa;
    private final Map<Integer, Integer> tiempoRestantePorTurista;
    private int serviciosSpaVendidos;

    public ZonaSanitarios(int capacidadMaxima, BigDecimal precioSpa) {
        super("Zona de Sanitarios", capacidadMaxima);
        this.precioSpa = precioSpa;
        this.tiempoRestantePorTurista = new HashMap<>();
        this.serviciosSpaVendidos = 0;
    }

    @Override
    public boolean ingresarTurista(Turista turista) {
        boolean ingreso = super.ingresarTurista(turista);
        if (ingreso) {
            tiempoRestantePorTurista.put(turista.getIdentificador(), 2);
        }
        return ingreso;
    }

    @Override
    public boolean retirarTurista(Turista turista) {
        tiempoRestantePorTurista.remove(turista.getIdentificador());
        return super.retirarTurista(turista);
    }

    public void avanzarTiempo() {
        var iterador = tiempoRestantePorTurista.entrySet().iterator();
        while (iterador.hasNext()) {
            var entrada = iterador.next();
            entrada.setValue(entrada.getValue() - 1);
        }
    }

    public boolean turistaTerminoUso(int identificadorTurista) {
        Integer tiempo = tiempoRestantePorTurista.get(identificadorTurista);
        return tiempo != null && tiempo <= 0;
    }

    public boolean contratarSpa(Turista turista) {
        if (!turista.puedePagar(precioSpa)) {
            return false;
        }
        turista.cobrar(precioSpa);
        serviciosSpaVendidos++;
        return true;
    }

    public BigDecimal getPrecioSpa() {
        return precioSpa;
    }

    public int getServiciosSpaVendidos() {
        return serviciosSpaVendidos;
    }

    @Override
    public String describirActividad() {
        return "Sanitarios de capacidad limitada y servicio adicional de SPA";
    }
}
