package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.modelo.enumeraciones.TipoIngreso;
import com.parque.dinosaurios.parque.Parque;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class HoraOfertas implements EventoAleatorio {

    private static final BigDecimal DESCUENTO = new BigDecimal("0.50");

    private String descripcionGenerada;

    public HoraOfertas() {
        this.descripcionGenerada = "Sin detalles";
    }

    @Override
    public TipoEvento getTipo() {
        return TipoEvento.HORA_DE_OFERTAS;
    }

    @Override
    public String getDescripcion() {
        return descripcionGenerada;
    }

    @Override
    public void ejecutar(Parque parque) {
        BigDecimal precioOferta = parque.getRecintoCentral().getPrecioSouvenir()
                .multiply(DESCUENTO)
                .setScale(2, RoundingMode.HALF_UP);

        List<Turista> dentro = parque.getTuristas().stream()
                .filter(t -> t.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE
                        || t.getEstado() == EstadoTurista.USANDO_SERVICIO)
                .toList();

        int souvenirsVendidos = 0;
        BigDecimal totalIngreso = BigDecimal.ZERO;

        for (Turista turista : dentro) {
            if (turista.puedePagar(precioOferta)) {
                turista.cobrar(precioOferta);
                parque.registrarIngreso(
                        TipoIngreso.SOUVENIR,
                        "Souvenir en hora de ofertas",
                        precioOferta);
                souvenirsVendidos++;
                totalIngreso = totalIngreso.add(precioOferta);
            }
        }

        descripcionGenerada = "Hora de ofertas, " + souvenirsVendidos
                + " souvenirs vendidos al 50%, ingreso " + totalIngreso;
    }
}
