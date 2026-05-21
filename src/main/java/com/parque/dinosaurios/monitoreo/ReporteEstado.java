package com.parque.dinosaurios.monitoreo;

import java.math.BigDecimal;

public class ReporteEstado {

    private final int paso;
    private final int turistasActivos;
    private final int turistasEnFila;
    private final int dinosauriosEnRecintos;
    private final int energiaDisponible;
    private final int porcentajeEnergia;
    private final int eventosOcurridos;
    private final int vehiculosEnUso;
    private final int vehiculosOperativos;
    private final BigDecimal ingresoAcumulado;
    private final BigDecimal gastoAcumulado;

    public ReporteEstado(int paso,
                         int turistasActivos,
                         int turistasEnFila,
                         int dinosauriosEnRecintos,
                         int energiaDisponible,
                         int porcentajeEnergia,
                         int eventosOcurridos,
                         int vehiculosEnUso,
                         int vehiculosOperativos,
                         BigDecimal ingresoAcumulado,
                         BigDecimal gastoAcumulado) {
        this.paso = paso;
        this.turistasActivos = turistasActivos;
        this.turistasEnFila = turistasEnFila;
        this.dinosauriosEnRecintos = dinosauriosEnRecintos;
        this.energiaDisponible = energiaDisponible;
        this.porcentajeEnergia = porcentajeEnergia;
        this.eventosOcurridos = eventosOcurridos;
        this.vehiculosEnUso = vehiculosEnUso;
        this.vehiculosOperativos = vehiculosOperativos;
        this.ingresoAcumulado = ingresoAcumulado;
        this.gastoAcumulado = gastoAcumulado;
    }

    public int getPaso() { return paso; }
    public int getTuristasActivos() { return turistasActivos; }
    public int getTuristasEnFila() { return turistasEnFila; }
    public int getDinosauriosEnRecintos() { return dinosauriosEnRecintos; }
    public int getEnergiaDisponible() { return energiaDisponible; }
    public int getPorcentajeEnergia() { return porcentajeEnergia; }
    public int getEventosOcurridos() { return eventosOcurridos; }
    public int getVehiculosEnUso() { return vehiculosEnUso; }
    public int getVehiculosOperativos() { return vehiculosOperativos; }
    public BigDecimal getIngresoAcumulado() { return ingresoAcumulado; }
    public BigDecimal getGastoAcumulado() { return gastoAcumulado; }
}
