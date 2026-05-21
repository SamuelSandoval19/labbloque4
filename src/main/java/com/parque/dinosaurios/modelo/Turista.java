package com.parque.dinosaurios.modelo;

import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;

import java.math.BigDecimal;

public class Turista {

    private final int identificador;
    private final String nombre;
    private BigDecimal dineroDisponible;
    private EstadoTurista estado;
    private String zonaActual;

    public Turista(int identificador, String nombre, BigDecimal dineroDisponible) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.dineroDisponible = dineroDisponible;
        this.estado = EstadoTurista.EN_FILA;
        this.zonaActual = "Fila de entrada";
    }

    public boolean puedePagar(BigDecimal costo) {
        return dineroDisponible.compareTo(costo) >= 0;
    }

    public void cobrar(BigDecimal costo) {
        if (!puedePagar(costo)) {
            throw new IllegalStateException(
                    "El turista " + nombre + " no tiene dinero suficiente");
        }
        this.dineroDisponible = dineroDisponible.subtract(costo);
    }

    public void moverAZona(String zona) {
        this.zonaActual = zona;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getDineroDisponible() {
        return dineroDisponible;
    }

    public EstadoTurista getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurista estado) {
        this.estado = estado;
    }

    public String getZonaActual() {
        return zonaActual;
    }

    @Override
    public String toString() {
        return String.format("Turista[id=%d, %s, dinero=%s, estado=%s, zona=%s]",
                identificador, nombre, dineroDisponible, estado, zonaActual);
    }
}
