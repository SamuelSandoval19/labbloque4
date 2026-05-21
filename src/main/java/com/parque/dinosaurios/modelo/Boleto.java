package com.parque.dinosaurios.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Boleto {

    private final int folio;
    private final int identificadorTurista;
    private final BigDecimal precio;
    private final LocalDateTime fechaEmision;

    public Boleto(int folio, int identificadorTurista, BigDecimal precio) {
        this.folio = folio;
        this.identificadorTurista = identificadorTurista;
        this.precio = precio;
        this.fechaEmision = LocalDateTime.now();
    }

    public int getFolio() {
        return folio;
    }

    public int getIdentificadorTurista() {
        return identificadorTurista;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    @Override
    public String toString() {
        return String.format("Boleto[folio=%d, turista=%d, precio=%s]",
                folio, identificadorTurista, precio);
    }
}
