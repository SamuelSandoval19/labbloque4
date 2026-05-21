package com.parque.dinosaurios.parque;

import com.parque.dinosaurios.persistencia.ConexionBaseDatos;

public final class AyudanteParque {

    private AyudanteParque() {
    }

    public static Parque construirParqueLimpio() {
        Parque.reiniciarParaPruebas();
        ConexionBaseDatos conexion = new ConexionBaseDatos();
        return Parque.obtenerInstancia(conexion);
    }
}
