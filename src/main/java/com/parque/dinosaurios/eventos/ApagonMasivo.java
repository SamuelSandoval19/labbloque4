package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;
import com.parque.dinosaurios.parque.Parque;

import java.math.BigDecimal;

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
        BigDecimal costoReparacion = parque.getPlantaEnergia().realizarMantenimiento();

        parque.registrarGasto(
                TipoGasto.REPARACION_PLANTA,
                "Reparacion de planta despues de apagon masivo",
                costoReparacion);

        descripcionGenerada = "Apagon masivo, planta reiniciada con costo de reparacion de "
                + costoReparacion;
    }
}
