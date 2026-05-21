package com.parque.dinosaurios.eventos;

import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;
import com.parque.dinosaurios.parque.Parque;

public interface EventoAleatorio {

    TipoEvento getTipo();

    String getDescripcion();

    void ejecutar(Parque parque);
}
