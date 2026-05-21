package com.parque.dinosaurios.eventos;

import java.util.Random;

public class GeneradorEventos {

    private final Random aleatorio;
    private final double probabilidadPorPaso;

    public GeneradorEventos(double probabilidadPorPaso) {
        this.aleatorio = new Random();
        this.probabilidadPorPaso = probabilidadPorPaso;
    }

    public GeneradorEventos(double probabilidadPorPaso, long semilla) {
        this.aleatorio = new Random(semilla);
        this.probabilidadPorPaso = probabilidadPorPaso;
    }

    public boolean ocurreEventoEnEstePaso() {
        return aleatorio.nextDouble() < probabilidadPorPaso;
    }

    public EventoAleatorio generarEvento() {
        int seleccion = aleatorio.nextInt(5);
        return switch (seleccion) {
            case 0 -> new EscapeDinosaurio();
            case 1 -> new ApagonMasivo();
            case 2 -> new TormentaTorrencial();
            case 3 -> new HoraOfertas();
            case 4 -> new FallaVehiculos();
            default -> new HoraOfertas();
        };
    }
}
