package com.parque.dinosaurios.menu;

import com.parque.dinosaurios.parque.Parque;
import com.parque.dinosaurios.persistencia.EventoRepositorio;
import com.parque.dinosaurios.persistencia.GastoRepositorio;
import com.parque.dinosaurios.persistencia.IngresoRepositorio;
import com.parque.dinosaurios.simulacion.MotorSimulacion;

import java.util.Scanner;

public class MenuPrincipal {

    private final Parque parque;
    private final MotorSimulacion motor;
    private final Scanner entrada;

    public MenuPrincipal(Parque parque, MotorSimulacion motor) {
        this.parque = parque;
        this.motor = motor;
        this.entrada = new Scanner(System.in);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarOpciones();
            String opcion = entrada.nextLine().trim();
            switch (opcion) {
                case "1" -> motor.ejecutar();
                case "2" -> mostrarEstadoActual();
                case "3" -> mostrarDinosaurios();
                case "4" -> mostrarTuristas();
                case "5" -> mostrarVehiculos();
                case "6" -> consultarIngresos();
                case "7" -> consultarGastos();
                case "8" -> consultarEventos();
                case "9" -> consultarInformacionGeneral();
                case "0" -> salir = true;
                default -> System.out.println("Opcion no reconocida, intente de nuevo.");
            }
        }
        System.out.println("Hasta luego.");
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("\"Parque de Dinosaurios\"");
        System.out.println(" 1. Ejecutar simulacion completa");
        System.out.println(" 2. Ver estado actual del parque");
        System.out.println(" 3. Listar dinosaurios");
        System.out.println(" 4. Listar turistas");
        System.out.println(" 5. Listar vehiculos");
        System.out.println(" 6. Consultar ingresos registrados");
        System.out.println(" 7. Consultar gastos registrados");
        System.out.println(" 8. Consultar eventos registrados");
        System.out.println(" 9. Centro de informacion general");
        System.out.println(" 0. Salir");
        System.out.print("Elige una opcion: ");
    }

    private void mostrarEstadoActual() {
        parque.publicarReporte(motor.getPasoActual());
    }

    private void mostrarDinosaurios() {
        System.out.println();
        System.out.println("Dinosaurios registrados: " + parque.getDinosaurios().size());
        parque.getDinosaurios().forEach(d -> System.out.println("  " + d));
    }

    private void mostrarTuristas() {
        System.out.println();
        System.out.println("Turistas registrados: " + parque.getTuristas().size());
        parque.getTuristas().forEach(t -> System.out.println("  " + t));
    }

    private void mostrarVehiculos() {
        System.out.println();
        System.out.println("Vehiculos del parque: " + parque.getVehiculos().size());
        parque.getVehiculos().forEach(v -> System.out.println("  " + v));
    }

    private void consultarIngresos() {
        IngresoRepositorio repo = parque.getIngresoRepositorio();
        System.out.println();
        System.out.println("Total registrado en BD: " + repo.sumarTotal());
        repo.listar().forEach(System.out::println);
    }

    private void consultarGastos() {
        GastoRepositorio repo = parque.getGastoRepositorio();
        System.out.println();
        System.out.println("Total registrado en BD: " + repo.sumarTotal());
        repo.listar().forEach(System.out::println);
    }

    private void consultarEventos() {
        EventoRepositorio repo = parque.getEventoRepositorio();
        System.out.println();
        System.out.println("Eventos registrados:");
        repo.listar().forEach(System.out::println);
        System.out.println();
        System.out.println("Conteo por tipo:");
        repo.contarPorTipo().forEach(c -> System.out.println("  " + c.tipo() + ": " + c.cantidad()));
    }

    private void consultarInformacionGeneral() {
        System.out.println();
        System.out.print("Tema a consultar (horario, mapa, seguridad): ");
        String tema = entrada.nextLine().trim();
        System.out.println(parque.getRecintoCentral().consultarInformacion(tema));
    }
}
