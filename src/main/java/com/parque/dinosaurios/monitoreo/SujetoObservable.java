package com.parque.dinosaurios.monitoreo;

import com.parque.dinosaurios.eventos.EventoAleatorio;

import java.util.ArrayList;
import java.util.List;

public abstract class SujetoObservable {

    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void quitarObservador(Observador observador) {
        observadores.remove(observador);
    }

    protected void notificarReporte(ReporteEstado reporte) {
        for (Observador observador : observadores) {
            observador.recibirReporte(reporte);
        }
    }

    protected void notificarEvento(EventoAleatorio evento, int pasoActual) {
        for (Observador observador : observadores) {
            observador.notificarEvento(evento, pasoActual);
        }
    }

    public int cantidadObservadores() {
        return observadores.size();
    }
}
