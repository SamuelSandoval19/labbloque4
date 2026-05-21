package com.parque.dinosaurios.zonas;

import java.math.BigDecimal;

public class PlantaEnergia extends Zona {

    private int energiaDisponible;
    private final int energiaMaxima;
    private final int consumoPorPaso;
    private final int energiaMinimaOperacion;
    private final BigDecimal costoUnidadEnergia;
    private final BigDecimal costoMantenimiento;
    private boolean enFalla;

    public PlantaEnergia(int energiaInicial,
                         int consumoPorPaso,
                         int energiaMinimaOperacion,
                         BigDecimal costoUnidadEnergia,
                         BigDecimal costoMantenimiento) {
        super("Planta de Energia", 0);
        this.energiaDisponible = energiaInicial;
        this.energiaMaxima = energiaInicial;
        this.consumoPorPaso = consumoPorPaso;
        this.energiaMinimaOperacion = energiaMinimaOperacion;
        this.costoUnidadEnergia = costoUnidadEnergia;
        this.costoMantenimiento = costoMantenimiento;
        this.enFalla = false;
    }

    public BigDecimal consumirEnergia() {
        if (enFalla) {
            return BigDecimal.ZERO;
        }
        int consumo = Math.min(consumoPorPaso, energiaDisponible);
        energiaDisponible -= consumo;
        return costoUnidadEnergia.multiply(BigDecimal.valueOf(consumo));
    }

    public BigDecimal realizarMantenimiento() {
        this.energiaDisponible = energiaMaxima;
        this.enFalla = false;
        return costoMantenimiento;
    }

    public void provocarFalla() {
        this.enFalla = true;
    }

    public boolean tieneEnergiaSuficiente() {
        return !enFalla && energiaDisponible >= energiaMinimaOperacion;
    }

    public int getEnergiaDisponible() {
        return energiaDisponible;
    }

    public int getEnergiaMaxima() {
        return energiaMaxima;
    }

    public boolean estaEnFalla() {
        return enFalla;
    }

    public int getPorcentajeCarga() {
        if (energiaMaxima == 0) {
            return 0;
        }
        return (int) ((energiaDisponible * 100.0) / energiaMaxima);
    }

    @Override
    public String describirActividad() {
        return "Suministra energia a todo el parque, sufre fallas aleatorias y genera costos operativos";
    }
}
