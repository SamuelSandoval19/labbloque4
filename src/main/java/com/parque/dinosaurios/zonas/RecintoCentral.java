package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Turista;

import java.math.BigDecimal;

public class RecintoCentral extends Zona {

    private final BigDecimal precioSouvenir;
    private int souvenirsVendidos;

    public RecintoCentral(int capacidadMaxima, BigDecimal precioSouvenir) {
        super("Recinto Central", capacidadMaxima);
        this.precioSouvenir = precioSouvenir;
        this.souvenirsVendidos = 0;
    }

    public boolean venderSouvenir(Turista turista) {
        if (!turista.puedePagar(precioSouvenir)) {
            return false;
        }
        turista.cobrar(precioSouvenir);
        souvenirsVendidos++;
        return true;
    }

    public String consultarInformacion(String tema) {
        return switch (tema.toLowerCase()) {
            case "horario" -> "El parque opera de 9:00 a 19:00 horas";
            case "mapa" -> "Cinco zonas conectadas a partir del Recinto Central";
            case "seguridad" -> "Por favor no cruce las barreras de los recintos";
            default -> "Acerquese a un trabajador para mas informacion";
        };
    }

    public BigDecimal getPrecioSouvenir() {
        return precioSouvenir;
    }

    public int getSouvenirsVendidos() {
        return souvenirsVendidos;
    }

    @Override
    public String describirActividad() {
        return "Punto principal, venta de souvenirs, centro de informacion y consola de consulta";
    }
}
