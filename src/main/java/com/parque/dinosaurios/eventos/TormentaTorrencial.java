package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.parque.Parque;

import java.util.List;

public class TormentaTorrencial implements EventoAleatorio {

    private String descripcionGenerada;

    public TormentaTorrencial() {
        this.descripcionGenerada = "Sin detalles";
    }

    @Override
    public TipoEvento getTipo() {
        return TipoEvento.TORMENTA_TORRENCIAL;
    }

    @Override
    public String getDescripcion() {
        return descripcionGenerada;
    }

    @Override
    public void ejecutar(Parque parque) {
        List<Turista> dentro = parque.getTuristas().stream()
                .filter(t -> t.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE)
                .toList();

        int refugiados = 0;
        for (Turista turista : dentro) {
            if (parque.refugiarTuristaEnRecintoCentral(turista)) {
                refugiados++;
            }
        }

        descripcionGenerada = "Tormenta torrencial, " + refugiados
                + " turistas refugiados en el Recinto Central";
    }
}
