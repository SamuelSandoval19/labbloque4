package com.parque.dinosaurios.monitoreo;

import com.parque.dinosaurios.eventos.EventoAleatorio;
import com.parque.dinosaurios.persistencia.EventoRepositorio;

public class RegistroEventos implements Observador {

    private final EventoRepositorio eventoRepositorio;
    private int totalRegistrados;

    public RegistroEventos(EventoRepositorio eventoRepositorio) {
        this.eventoRepositorio = eventoRepositorio;
        this.totalRegistrados = 0;
    }

    @Override
    public void notificarEvento(EventoAleatorio evento, int pasoActual) {
        eventoRepositorio.guardar(evento.getTipo(), evento.getDescripcion(), pasoActual, true);
        totalRegistrados++;
    }

    public int getTotalRegistrados() {
        return totalRegistrados;
    }
}
