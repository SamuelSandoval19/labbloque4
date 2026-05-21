package com.parque.dinosaurios.modelo;

public class Trabajador {

    private final int identificador;
    private final String nombre;
    private final String puesto;
    private boolean disponible;

    public Trabajador(int identificador, String nombre, String puesto) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.puesto = puesto;
        this.disponible = true;
    }

    public void asignarTarea() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    @Override
    public String toString() {
        return String.format("Trabajador[id=%d, %s, puesto=%s, disponible=%s]",
                identificador, nombre, puesto, disponible);
    }
}
