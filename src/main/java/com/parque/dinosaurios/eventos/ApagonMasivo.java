package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.parque.Parque;

public class ApagonMasivo implements EventoAleatorio {

    private String descripcionGenerada;

    public ApagonMasivo() {
        this.descripcionGenerada = "Sin detalles";
    }

    @Override
    public TipoEvento getTipo() {
        return TipoEvento.APAGON_MASIVO;
    }

    @Override
    public String getDescripcion() {
        return descripcionGenerada;
    }

    @Override
    public void ejecutar(Parque parque) {
        parque.getPlantaEnergia().provocarFalla();
        descripcionGenerada = "Apagon masivo, la planta esta fuera de servicio. "
                + "Se requiere un tecnico y un vehiculo disponible para repararla.";
    }
}
