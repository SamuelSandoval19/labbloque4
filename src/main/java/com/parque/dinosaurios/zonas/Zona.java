package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Turista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Zona {

    protected final String nombre;
    protected final int capacidadMaxima;
    protected final List<Turista> turistasDentro;

    protected Zona(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.turistasDentro = new ArrayList<>();
    }

    public boolean tieneEspacio() {
        return turistasDentro.size() < capacidadMaxima;
    }

    public boolean ingresarTurista(Turista turista) {
        if (!tieneEspacio()) {
            return false;
        }
        turistasDentro.add(turista);
        turista.moverAZona(nombre);
        return true;
    }

    public boolean retirarTurista(Turista turista) {
        return turistasDentro.remove(turista);
    }

    public void vaciar() {
        turistasDentro.clear();
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getOcupacion() {
        return turistasDentro.size();
    }

    public List<Turista> getTuristasDentro() {
        return Collections.unmodifiableList(turistasDentro);
    }

    public abstract String describirActividad();
}
