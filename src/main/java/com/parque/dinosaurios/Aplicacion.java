package com.parque.dinosaurios;

import com.parque.dinosaurios.menu.MenuPrincipal;
import com.parque.dinosaurios.monitoreo.MonitorConsola;
import com.parque.dinosaurios.monitoreo.RegistroEventos;
import com.parque.dinosaurios.parque.Parque;
import com.parque.dinosaurios.persistencia.ConexionBaseDatos;
import com.parque.dinosaurios.simulacion.MotorSimulacion;

public class Aplicacion {

    public static void main(String[] args) {
        System.out.println("Iniciando Parque Turistico de Dinosaurios");

        ConexionBaseDatos conexion = new ConexionBaseDatos();
        boolean conectado = conexion.intentarConectar();
        if (conectado) {
            conexion.ejecutarMigraciones();
        }

        Parque parque = Parque.obtenerInstancia(conexion);

        parque.agregarObservador(new MonitorConsola());
        parque.agregarObservador(new RegistroEventos(parque.getEventoRepositorio()));

        MotorSimulacion motor = new MotorSimulacion(parque);

        MenuPrincipal menu = new MenuPrincipal(parque, motor);
        menu.iniciar();
    }
}
