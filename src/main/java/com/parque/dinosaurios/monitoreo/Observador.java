package com.parque.dinosaurios.monitoreo;

import com.parque.dinosaurios.eventos.EventoAleatorio;

public interface Observador {

    default void recibirReporte(ReporteEstado reporte) {
    }

    default void notificarEvento(EventoAleatorio evento, int pasoActual) {
    }
}
