package com.parque.dinosaurios.modelo;

public class Vehiculo {

    public enum EstadoVehiculo {
        DISPONIBLE,
        EN_USO,
        AVERIADO
    }

    private final int identificador;
    private final String tipo;
    private final int pasosReparacion;
    private EstadoVehiculo estado;
    private int pasosRestantesReparacion;

    public Vehiculo(int identificador, String tipo, int pasosReparacion) {
        this.identificador = identificador;
        this.tipo = tipo;
        this.pasosReparacion = pasosReparacion;
        this.estado = EstadoVehiculo.DISPONIBLE;
        this.pasosRestantesReparacion = 0;
    }

    public void ponerEnUso() {
        if (estado != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalStateException(
                    "El vehiculo " + identificador + " no esta disponible");
        }
        estado = EstadoVehiculo.EN_USO;
    }

    public void liberar() {
        if (estado == EstadoVehiculo.EN_USO) {
            estado = EstadoVehiculo.DISPONIBLE;
        }
    }

    public void averiar() {
        estado = EstadoVehiculo.AVERIADO;
        pasosRestantesReparacion = pasosReparacion;
    }

    public void avanzarReparacion() {
        if (estado != EstadoVehiculo.AVERIADO) {
            return;
        }
        if (pasosRestantesReparacion > 0) {
            pasosRestantesReparacion--;
        }
        if (pasosRestantesReparacion == 0) {
            estado = EstadoVehiculo.DISPONIBLE;
        }
    }

    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    public boolean estaEnUso() {
        return estado == EstadoVehiculo.EN_USO;
    }

    public boolean estaAveriado() {
        return estado == EstadoVehiculo.AVERIADO;
    }

    public boolean estaOperativo() {
        return estado != EstadoVehiculo.AVERIADO;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getTipo() {
        return tipo;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public int getPasosRestantesReparacion() {
        return pasosRestantesReparacion;
    }

    @Override
    public String toString() {
        return String.format("Vehiculo[id=%d, tipo=%s, estado=%s, pasosRestReparacion=%d]",
                identificador, tipo, estado, pasosRestantesReparacion);
    }
}
