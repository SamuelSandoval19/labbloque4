package com.parque.dinosaurios.modelo;

public class Vehiculo {

    private final int identificador;
    private final String tipo;
    private boolean operativo;
    private boolean enUso;

    public Vehiculo(int identificador, String tipo) {
        this.identificador = identificador;
        this.tipo = tipo;
        this.operativo = true;
        this.enUso = false;
    }

    public void ponerEnUso() {
        if (!operativo) {
            throw new IllegalStateException("El vehiculo " + identificador + " esta averiado");
        }
        this.enUso = true;
    }

    public void liberar() {
        this.enUso = false;
    }

    public void averiar() {
        this.operativo = false;
        this.enUso = false;
    }

    public void reparar() {
        this.operativo = true;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean estaOperativo() {
        return operativo;
    }

    public boolean estaEnUso() {
        return enUso;
    }

    @Override
    public String toString() {
        return String.format("Vehiculo[id=%d, tipo=%s, operativo=%s, enUso=%s]",
                identificador, tipo, operativo, enUso);
    }
}
