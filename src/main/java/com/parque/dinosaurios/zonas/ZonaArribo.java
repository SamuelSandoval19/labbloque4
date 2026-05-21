package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Boleto;
import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ZonaArribo extends Zona {

    private final Queue<Turista> filaDeEspera;
    private final List<Boleto> boletosVendidos;
    private final BigDecimal precioBoleto;
    private int folioActual;

    public ZonaArribo(int capacidadMaxima, BigDecimal precioBoleto) {
        super("Zona de Arribo", capacidadMaxima);
        this.filaDeEspera = new LinkedList<>();
        this.boletosVendidos = new ArrayList<>();
        this.precioBoleto = precioBoleto;
        this.folioActual = 1;
    }

    public void agregarAFila(Turista turista) {
        turista.setEstado(EstadoTurista.EN_FILA);
        filaDeEspera.add(turista);
    }

    public Boleto venderBoleto(Turista turista) {
        if (!turista.puedePagar(precioBoleto)) {
            return null;
        }
        turista.cobrar(precioBoleto);
        Boleto boleto = new Boleto(folioActual++, turista.getIdentificador(), precioBoleto);
        boletosVendidos.add(boleto);
        return boleto;
    }

    public Turista atenderSiguienteDeFila() {
        if (filaDeEspera.isEmpty() || !tieneEspacio()) {
            return null;
        }
        Turista siguiente = filaDeEspera.poll();
        Boleto boleto = venderBoleto(siguiente);
        if (boleto == null) {
            siguiente.setEstado(EstadoTurista.FUERA_DEL_PARQUE);
            return null;
        }
        ingresarTurista(siguiente);
        siguiente.setEstado(EstadoTurista.DENTRO_DEL_PARQUE);
        return siguiente;
    }

    public void registrarSalida(Turista turista) {
        retirarTurista(turista);
        turista.setEstado(EstadoTurista.FUERA_DEL_PARQUE);
    }

    public int getTuristasEnFila() {
        return filaDeEspera.size();
    }

    public List<Boleto> getBoletosVendidos() {
        return boletosVendidos;
    }

    public BigDecimal getPrecioBoleto() {
        return precioBoleto;
    }

    @Override
    public String describirActividad() {
        return "Punto de entrada y salida del parque, venta de boletos y control de aforo";
    }
}
