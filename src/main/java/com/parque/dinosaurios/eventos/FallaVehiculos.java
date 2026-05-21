package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.Vehiculo;
import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;
import com.parque.dinosaurios.parque.Parque;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class FallaVehiculos implements EventoAleatorio {

    private final Random aleatorio;
    private String descripcionGenerada;

    public FallaVehiculos() {
        this.aleatorio = new Random();
        this.descripcionGenerada = "Sin detalles";
    }

    @Override
    public TipoEvento getTipo() {
        return TipoEvento.FALLA_DE_VEHICULOS;
    }

    @Override
    public String getDescripcion() {
        return descripcionGenerada;
    }

    @Override
    public void ejecutar(Parque parque) {
        List<Vehiculo> operativos = parque.getVehiculos().stream()
                .filter(Vehiculo::estaOperativo)
                .toList();

        if (operativos.isEmpty()) {
            descripcionGenerada = "Intento de averia pero no hay vehiculos operativos";
            return;
        }

        int aAveriarse = 1 + aleatorio.nextInt(Math.min(2, operativos.size()));
        BigDecimal costoTotal = BigDecimal.ZERO;
        BigDecimal costoUnitario = parque.getCostoReparacionVehiculo();

        for (int i = 0; i < aAveriarse; i++) {
            Vehiculo afectado = operativos.get(i);
            afectado.averiar();
            afectado.reparar();
            costoTotal = costoTotal.add(costoUnitario);
        }

        parque.registrarGasto(
                TipoGasto.REPARACION_VEHICULO,
                "Reparacion de " + aAveriarse + " vehiculos averiados",
                costoTotal);

        descripcionGenerada = aAveriarse + " vehiculos averiados y reparados, costo total " + costoTotal;
    }
}
