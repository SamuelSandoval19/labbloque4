package com.parque.dinosaurios.simulacion;

import com.parque.dinosaurios.configuracion.ConfiguracionParque;
import com.parque.dinosaurios.eventos.EventoAleatorio;
import com.parque.dinosaurios.eventos.GeneradorEventos;
import com.parque.dinosaurios.modelo.Dinosaurio;
import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.Vehiculo;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;
import com.parque.dinosaurios.modelo.enumeraciones.TipoIngreso;
import com.parque.dinosaurios.parque.Parque;
import com.parque.dinosaurios.zonas.RecintoObservacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MotorSimulacion {

    private final Parque parque;
    private final GeneradorEventos generadorEventos;
    private final Random aleatorio;
    private final int totalPasos;
    private final int intervaloMonitoreo;

    private int pasoActual;
    private boolean detenida;

    public MotorSimulacion(Parque parque) {
        this(parque, new Random());
    }

    public MotorSimulacion(Parque parque, Random aleatorio) {
        this.parque = parque;
        this.aleatorio = aleatorio;

        ConfiguracionParque cfg = ConfiguracionParque.obtenerInstancia();
        this.totalPasos = cfg.obtenerEntero("parque.simulacion.pasos");
        this.intervaloMonitoreo = cfg.obtenerEntero("parque.simulacion.intervalo.monitoreo");
        this.generadorEventos = new GeneradorEventos(
                cfg.obtenerDecimal("parque.simulacion.probabilidad.evento"));
        this.pasoActual = 0;
        this.detenida = false;
    }

    public void ejecutar() {
        System.out.println();
        System.out.println("Comienza la simulacion del parque, total de pasos: " + totalPasos);

        while (pasoActual < totalPasos && !detenida) {
            pasoActual++;
            ejecutarPaso();
        }

        cerrarParque();
        imprimirReporteFinal();
    }

    void ejecutarPaso() {
        atenderEntrada();
        moverTuristasActivos();
        consumirEnergia();
        avanzarSanitarios();
        atenderRecintosObservacion();
        revisarEventoAleatorio();
        sacarTuristasSatisfechos();

        if (pasoActual % intervaloMonitoreo == 0) {
            parque.publicarReporte(pasoActual);
        }
    }

    private void atenderEntrada() {
        int intentos = Math.min(5, parque.getZonaArribo().getTuristasEnFila());
        for (int i = 0; i < intentos; i++) {
            Turista atendido = parque.getZonaArribo().atenderSiguienteDeFila();
            if (atendido == null) {
                break;
            }
            parque.registrarIngreso(
                    TipoIngreso.BOLETO,
                    "Venta de boleto a " + atendido.getNombre(),
                    parque.getZonaArribo().getPrecioBoleto());
        }
    }

    private void moverTuristasActivos() {
        List<Turista> activos = new ArrayList<>();
        for (Turista turista : parque.getTuristas()) {
            if (turista.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE) {
                activos.add(turista);
            }
        }

        for (Turista turista : activos) {
            int destino = aleatorio.nextInt(4);
            switch (destino) {
                case 0 -> visitarRecintoCentral(turista);
                case 1 -> visitarSanitarios(turista);
                case 2 -> visitarRecintoObservacion(turista);
                case 3 -> {
                    // se queda donde esta, recorriendo
                }
            }
        }
    }

    private void visitarRecintoCentral(Turista turista) {
        if (!parque.getRecintoCentral().tieneEspacio()) {
            return;
        }
        parque.getRecintoCentral().ingresarTurista(turista);
        if (aleatorio.nextDouble() < 0.4
                && parque.getRecintoCentral().venderSouvenir(turista)) {
            parque.registrarIngreso(
                    TipoIngreso.SOUVENIR,
                    "Souvenir comprado por " + turista.getNombre(),
                    parque.getRecintoCentral().getPrecioSouvenir());
        }
        parque.getRecintoCentral().retirarTurista(turista);
    }

    private void visitarSanitarios(Turista turista) {
        if (!parque.getZonaSanitarios().tieneEspacio()) {
            return;
        }
        parque.getZonaSanitarios().ingresarTurista(turista);
        turista.setEstado(EstadoTurista.USANDO_SERVICIO);

        if (aleatorio.nextDouble() < 0.2
                && parque.getZonaSanitarios().contratarSpa(turista)) {
            parque.registrarIngreso(
                    TipoIngreso.SERVICIO_SPA,
                    "Servicio de SPA para " + turista.getNombre(),
                    parque.getZonaSanitarios().getPrecioSpa());
        }
    }

    private void visitarRecintoObservacion(Turista turista) {
        List<RecintoObservacion> recintos = parque.getRecintosObservacion();
        if (recintos.isEmpty()) {
            return;
        }
        RecintoObservacion elegido = recintos.get(aleatorio.nextInt(recintos.size()));
        if (!elegido.tieneEspacio()) {
            return;
        }
        if (!elegido.cobrarEntrada(turista)) {
            return;
        }
        parque.registrarIngreso(
                TipoIngreso.RECINTO_OBSERVACION,
                "Entrada al " + elegido.getNombre(),
                elegido.getCostoEntrada());

        elegido.ingresarTurista(turista);
        elegido.retirarTurista(turista);

        usarVehiculoAleatorio();
    }

    private void usarVehiculoAleatorio() {
        List<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo vehiculo : parque.getVehiculos()) {
            if (vehiculo.estaOperativo() && !vehiculo.estaEnUso()) {
                disponibles.add(vehiculo);
            }
        }
        if (disponibles.isEmpty()) {
            return;
        }
        Vehiculo elegido = disponibles.get(aleatorio.nextInt(disponibles.size()));
        elegido.ponerEnUso();
        elegido.liberar();
    }

    private void atenderRecintosObservacion() {
        for (RecintoObservacion recinto : parque.getRecintosObservacion()) {
            if (recinto.getOcupacion() > 0) {
                recinto.aplicarEncuesta();
            }
        }
    }

    private void consumirEnergia() {
        BigDecimal costo = parque.getPlantaEnergia().consumirEnergia();
        if (costo.compareTo(BigDecimal.ZERO) > 0) {
            parque.registrarGasto(
                    TipoGasto.CONSUMO_ENERGIA,
                    "Consumo de energia en paso " + pasoActual,
                    costo);
        }
        if (!parque.getPlantaEnergia().tieneEnergiaSuficiente()) {
            BigDecimal costoMantto = parque.getPlantaEnergia().realizarMantenimiento();
            parque.registrarGasto(
                    TipoGasto.MANTENIMIENTO,
                    "Mantenimiento programado por carga baja",
                    costoMantto);
        }
    }

    private void avanzarSanitarios() {
        parque.getZonaSanitarios().avanzarTiempo();
        List<Turista> aSalir = new ArrayList<>();
        for (Turista turista : parque.getZonaSanitarios().getTuristasDentro()) {
            if (parque.getZonaSanitarios().turistaTerminoUso(turista.getIdentificador())) {
                aSalir.add(turista);
            }
        }
        for (Turista turista : aSalir) {
            parque.getZonaSanitarios().retirarTurista(turista);
            turista.setEstado(EstadoTurista.DENTRO_DEL_PARQUE);
        }
    }

    private void revisarEventoAleatorio() {
        if (!generadorEventos.ocurreEventoEnEstePaso()) {
            return;
        }
        EventoAleatorio evento = generadorEventos.generarEvento();
        evento.ejecutar(parque);
        parque.contabilizarEvento(evento, pasoActual);
    }

    private void sacarTuristasSatisfechos() {
        List<Turista> aSalir = new ArrayList<>();
        for (Turista turista : parque.getTuristas()) {
            if (turista.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE
                    && aleatorio.nextDouble() < 0.05) {
                aSalir.add(turista);
            }
        }
        for (Turista turista : aSalir) {
            parque.getZonaArribo().registrarSalida(turista);
        }
    }

    private void cerrarParque() {
        for (Turista turista : parque.getTuristas()) {
            if (turista.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE
                    || turista.getEstado() == EstadoTurista.USANDO_SERVICIO) {
                turista.setEstado(EstadoTurista.SALIENDO);
            }
        }
        for (Dinosaurio dinosaurio : parque.getDinosaurios()) {
            dinosaurio.alimentar();
        }
    }

    private void imprimirReporteFinal() {
        System.out.println();
        System.out.println("\"Fin de la simulacion\"");
        parque.publicarReporte(pasoActual);
        System.out.println();
        System.out.println("Resumen final:");
        System.out.println("  Ingresos totales : " + parque.getIngresoAcumulado());
        System.out.println("  Gastos totales   : " + parque.getGastoAcumulado());
        System.out.println("  Balance          : "
                + parque.getIngresoAcumulado().subtract(parque.getGastoAcumulado()));
        System.out.println("  Eventos          : " + parque.getEventosOcurridos());
    }

    public void detener() {
        this.detenida = true;
    }

    public int getPasoActual() {
        return pasoActual;
    }
}
