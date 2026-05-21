package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.Dinosaurio;
import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoDinosaurio;
import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.parque.Parque;

import java.util.List;
import java.util.Random;

public class EscapeDinosaurio implements EventoAleatorio {

    private final Random aleatorio;
    private String descripcionGenerada;

    public EscapeDinosaurio() {
        this.aleatorio = new Random();
        this.descripcionGenerada = "Sin detalles";
    }

    @Override
    public TipoEvento getTipo() {
        return TipoEvento.ESCAPE_DINOSAURIO;
    }

    @Override
    public String getDescripcion() {
        return descripcionGenerada;
    }

    @Override
    public void ejecutar(Parque parque) {
        List<Dinosaurio> enRecinto = parque.getDinosaurios().stream()
                .filter(Dinosaurio::estaEnRecinto)
                .toList();

        if (enRecinto.isEmpty()) {
            descripcionGenerada = "Intento de escape pero todos los dinosaurios estan controlados";
            return;
        }

        Dinosaurio fugado = enRecinto.get(aleatorio.nextInt(enRecinto.size()));
        fugado.marcarComoEscapado();

        StringBuilder detalle = new StringBuilder();
        detalle.append("Escape de ").append(fugado.getNombre()).append(" (")
                .append(fugado.getTipo()).append(")");

        if (fugado.getTipo() == TipoDinosaurio.CARNIVORO) {
            List<Turista> dentro = parque.getTuristas().stream()
                    .filter(t -> t.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE
                            || t.getEstado() == EstadoTurista.USANDO_SERVICIO)
                    .toList();

            if (!dentro.isEmpty() && aleatorio.nextDouble() < 0.4) {
                Turista atacado = dentro.get(aleatorio.nextInt(dentro.size()));
                atacado.setEstado(EstadoTurista.HERIDO);
                detalle.append(". Turista herido: ").append(atacado.getNombre());
            }
        }

        fugado.regresarAlRecinto();
        detalle.append(". Dinosaurio devuelto al recinto.");
        descripcionGenerada = detalle.toString();
    }
}
