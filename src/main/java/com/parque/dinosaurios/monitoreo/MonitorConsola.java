package com.parque.dinosaurios.monitoreo;

import com.parque.dinosaurios.eventos.EventoAleatorio;

public class MonitorConsola implements Observador {

    @Override
    public void recibirReporte(ReporteEstado reporte) {
        System.out.println();
        System.out.println("\"Monitoreo del parque\"");
        System.out.printf("Paso de simulacion : %d%n", reporte.getPaso());
        System.out.printf("Turistas activos   : %d (en fila: %d)%n",
                reporte.getTuristasActivos(), reporte.getTuristasEnFila());
        System.out.printf("Dinosaurios        : %d en recintos%n",
                reporte.getDinosauriosEnRecintos());
        System.out.printf("Energia disponible : %d (%d%%)%n",
                reporte.getEnergiaDisponible(), reporte.getPorcentajeEnergia());
        System.out.printf("Vehiculos          : %d en uso, %d operativos%n",
                reporte.getVehiculosEnUso(), reporte.getVehiculosOperativos());
        System.out.printf("Eventos ocurridos  : %d%n", reporte.getEventosOcurridos());
        System.out.printf("Ingresos / Gastos  : %s / %s%n",
                reporte.getIngresoAcumulado(), reporte.getGastoAcumulado());
    }

    @Override
    public void notificarEvento(EventoAleatorio evento, int pasoActual) {
        System.out.println();
        System.out.println(">>> EVENTO en paso " + pasoActual + ": " + evento.getTipo());
        System.out.println("    " + evento.getDescripcion());
    }
}
