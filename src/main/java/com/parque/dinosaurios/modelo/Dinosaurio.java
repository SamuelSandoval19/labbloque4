package com.parque.dinosaurios.modelo;

import com.parque.dinosaurios.modelo.enumeraciones.TipoDinosaurio;

public class Dinosaurio {

    private final int identificador;
    private final String nombre;
    private final TipoDinosaurio tipo;
    private int nivelHambre;
    private boolean enRecinto;

    public Dinosaurio(int identificador, String nombre, TipoDinosaurio tipo) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivelHambre = 0;
        this.enRecinto = true;
    }

    public void alimentar() {
        this.nivelHambre = 0;
    }

    public void aumentarHambre() {
        if (nivelHambre < 10) {
            nivelHambre++;
        }
    }

    public boolean estaHambriento() {
        return nivelHambre >= 7;
    }

    public void marcarComoEscapado() {
        this.enRecinto = false;
    }

    public void regresarAlRecinto() {
        this.enRecinto = true;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDinosaurio getTipo() {
        return tipo;
    }

    public int getNivelHambre() {
        return nivelHambre;
    }

    public boolean estaEnRecinto() {
        return enRecinto;
    }

    @Override
    public String toString() {
        return String.format("Dinosaurio[id=%d, %s, tipo=%s, hambre=%d, enRecinto=%s]",
                identificador, nombre, tipo, nivelHambre, enRecinto);
    }
}
