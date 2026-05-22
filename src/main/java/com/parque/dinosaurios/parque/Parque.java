package com.parque.dinosaurios.parque;

import com.parque.dinosaurios.configuracion.ConfiguracionParque;
import com.parque.dinosaurios.eventos.EventoAleatorio;
import com.parque.dinosaurios.modelo.Dinosaurio;
import com.parque.dinosaurios.modelo.Trabajador;
import com.parque.dinosaurios.modelo.Turista;
import com.parque.dinosaurios.modelo.Vehiculo;
import com.parque.dinosaurios.modelo.enumeraciones.EstadoTurista;
import com.parque.dinosaurios.modelo.enumeraciones.TipoDinosaurio;
import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;
import com.parque.dinosaurios.modelo.enumeraciones.TipoIngreso;
import com.parque.dinosaurios.monitoreo.ReporteEstado;
import com.parque.dinosaurios.monitoreo.SujetoObservable;
import com.parque.dinosaurios.persistencia.ConexionBaseDatos;
import com.parque.dinosaurios.persistencia.EventoRepositorio;
import com.parque.dinosaurios.persistencia.GastoRepositorio;
import com.parque.dinosaurios.persistencia.IngresoRepositorio;
import com.parque.dinosaurios.zonas.PlantaEnergia;
import com.parque.dinosaurios.zonas.RecintoCentral;
import com.parque.dinosaurios.zonas.RecintoObservacion;
import com.parque.dinosaurios.zonas.ZonaArribo;
import com.parque.dinosaurios.zonas.ZonaSanitarios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Parque extends SujetoObservable {

    private static Parque instancia;

    private final List<Turista> turistas;
    private final List<Dinosaurio> dinosaurios;
    private final List<Trabajador> trabajadores;
    private final List<Vehiculo> vehiculos;

    private final ZonaArribo zonaArribo;
    private final RecintoCentral recintoCentral;
    private final ZonaSanitarios zonaSanitarios;
    private final PlantaEnergia plantaEnergia;
    private final List<RecintoObservacion> recintosObservacion;

    private final IngresoRepositorio ingresoRepositorio;
    private final GastoRepositorio gastoRepositorio;
    private final EventoRepositorio eventoRepositorio;

    private final BigDecimal costoReparacionVehiculo;

    private BigDecimal ingresoAcumulado;
    private BigDecimal gastoAcumulado;
    private int eventosOcurridos;

    private Parque(ConfiguracionParque cfg, ConexionBaseDatos conexion) {
        this.turistas = new ArrayList<>();
        this.dinosaurios = new ArrayList<>();
        this.trabajadores = new ArrayList<>();
        this.vehiculos = new ArrayList<>();
        this.recintosObservacion = new ArrayList<>();

        this.zonaArribo = new ZonaArribo(
                cfg.obtenerEntero("parque.capacidad.maxima"),
                cfg.obtenerMonto("parque.precio.boleto"));

        this.recintoCentral = new RecintoCentral(
                cfg.obtenerEntero("parque.capacidad.maxima"),
                cfg.obtenerMonto("parque.precio.souvenir"));

        this.zonaSanitarios = new ZonaSanitarios(
                cfg.obtenerEntero("parque.sanitarios.capacidad"),
                cfg.obtenerMonto("parque.precio.spa"));

        this.plantaEnergia = new PlantaEnergia(
                cfg.obtenerEntero("parque.energia.inicial"),
                cfg.obtenerEntero("parque.energia.consumo.paso"),
                cfg.obtenerEntero("parque.energia.minima.operacion"),
                cfg.obtenerMonto("parque.costo.energia.unidad"),
                cfg.obtenerMonto("parque.costo.mantenimiento"));

        this.recintosObservacion.add(new RecintoObservacion(
                "Carnivoros",
                cfg.obtenerEntero("parque.observatorio.capacidad"),
                cfg.obtenerMonto("parque.precio.observacion"),
                12345L));
        this.recintosObservacion.add(new RecintoObservacion(
                "Herbivoros",
                cfg.obtenerEntero("parque.observatorio.capacidad"),
                cfg.obtenerMonto("parque.precio.observacion"),
                67890L));

        this.ingresoRepositorio = new IngresoRepositorio(conexion);
        this.gastoRepositorio = new GastoRepositorio(conexion);
        this.eventoRepositorio = new EventoRepositorio(conexion);

        this.costoReparacionVehiculo = cfg.obtenerMonto("parque.costo.reparacion.vehiculo");

        this.ingresoAcumulado = BigDecimal.ZERO;
        this.gastoAcumulado = BigDecimal.ZERO;
        this.eventosOcurridos = 0;

        poblarTuristasIniciales(cfg.obtenerEntero("parque.turistas.iniciales"));
        poblarDinosaurios(
                cfg.obtenerEntero("parque.dinosaurios.carnivoros"),
                cfg.obtenerEntero("parque.dinosaurios.herbivoros"));
        poblarTrabajadores(cfg.obtenerEntero("parque.trabajadores"));
        poblarVehiculos(cfg.obtenerEntero("parque.vehiculos"));
    }

    public static synchronized Parque obtenerInstancia(ConexionBaseDatos conexion) {
        if (instancia == null) {
            instancia = new Parque(ConfiguracionParque.obtenerInstancia(), conexion);
        }
        return instancia;
    }

    public static synchronized Parque obtenerInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("El parque aun no ha sido inicializado");
        }
        return instancia;
    }

    private void poblarTuristasIniciales(int cantidad) {
        BigDecimal dineroBase = new BigDecimal("1500.00");
        for (int i = 1; i <= cantidad; i++) {
            Turista turista = new Turista(i, "Turista_" + i, dineroBase);
            turistas.add(turista);
            zonaArribo.agregarAFila(turista);
        }
    }

    private void poblarDinosaurios(int carnivoros, int herbivoros) {
        int id = 1;
        for (int i = 0; i < carnivoros; i++) {
            dinosaurios.add(new Dinosaurio(id, "Carnivoro_" + id, TipoDinosaurio.CARNIVORO));
            id++;
        }
        for (int i = 0; i < herbivoros; i++) {
            dinosaurios.add(new Dinosaurio(id, "Herbivoro_" + id, TipoDinosaurio.HERBIVORO));
            id++;
        }
    }

    private void poblarTrabajadores(int cantidad) {
        String[] puestos = {"Cuidador", "Guia", "Vigilante", "Mantenimiento", "Cajero"};
        for (int i = 1; i <= cantidad; i++) {
            String puesto = puestos[(i - 1) % puestos.length];
            trabajadores.add(new Trabajador(i, "Trabajador_" + i, puesto));
        }
    }

    private void poblarVehiculos(int cantidad) {
        String[] tipos = {"Jeep", "Buggy", "Camioneta"};
        int pasosReparacion = ConfiguracionParque.obtenerInstancia()
                .obtenerEntero("parque.vehiculos.pasos.reparacion");
        for (int i = 1; i <= cantidad; i++) {
            vehiculos.add(new Vehiculo(i, tipos[(i - 1) % tipos.length], pasosReparacion));
        }
    }

    public void registrarIngreso(TipoIngreso tipo, String descripcion, BigDecimal monto) {
        ingresoRepositorio.guardar(tipo, descripcion, monto);
        ingresoAcumulado = ingresoAcumulado.add(monto);
    }

    public void registrarGasto(TipoGasto tipo, String descripcion, BigDecimal monto) {
        gastoRepositorio.guardar(tipo, descripcion, monto);
        gastoAcumulado = gastoAcumulado.add(monto);
    }

    public void contabilizarEvento(EventoAleatorio evento, int pasoActual) {
        eventosOcurridos++;
        notificarEvento(evento, pasoActual);
    }

    public void publicarReporte(int paso) {
        ReporteEstado reporte = construirReporte(paso);
        notificarReporte(reporte);
    }

    public ReporteEstado construirReporte(int paso) {
        int activos = (int) turistas.stream()
                .filter(t -> t.getEstado() == EstadoTurista.DENTRO_DEL_PARQUE
                        || t.getEstado() == EstadoTurista.USANDO_SERVICIO)
                .count();

        int enRecinto = (int) dinosaurios.stream()
                .filter(Dinosaurio::estaEnRecinto)
                .count();

        int enUso = (int) vehiculos.stream().filter(Vehiculo::estaEnUso).count();
        int operativos = (int) vehiculos.stream().filter(Vehiculo::estaOperativo).count();

        return new ReporteEstado(
                paso,
                activos,
                zonaArribo.getTuristasEnFila(),
                enRecinto,
                plantaEnergia.getEnergiaDisponible(),
                plantaEnergia.getPorcentajeCarga(),
                eventosOcurridos,
                enUso,
                operativos,
                ingresoAcumulado,
                gastoAcumulado);
    }

    public boolean refugiarTuristaEnRecintoCentral(Turista turista) {
        if (recintoCentral.tieneEspacio()) {
            return recintoCentral.ingresarTurista(turista);
        }
        return false;
    }

    public List<Turista> getTuristas() {
        return Collections.unmodifiableList(turistas);
    }

    public List<Dinosaurio> getDinosaurios() {
        return Collections.unmodifiableList(dinosaurios);
    }

    public List<Trabajador> getTrabajadores() {
        return Collections.unmodifiableList(trabajadores);
    }

    public List<Vehiculo> getVehiculos() {
        return Collections.unmodifiableList(vehiculos);
    }

    public ZonaArribo getZonaArribo() {
        return zonaArribo;
    }

    public RecintoCentral getRecintoCentral() {
        return recintoCentral;
    }

    public ZonaSanitarios getZonaSanitarios() {
        return zonaSanitarios;
    }

    public PlantaEnergia getPlantaEnergia() {
        return plantaEnergia;
    }

    public List<RecintoObservacion> getRecintosObservacion() {
        return Collections.unmodifiableList(recintosObservacion);
    }

    public IngresoRepositorio getIngresoRepositorio() {
        return ingresoRepositorio;
    }

    public GastoRepositorio getGastoRepositorio() {
        return gastoRepositorio;
    }

    public EventoRepositorio getEventoRepositorio() {
        return eventoRepositorio;
    }

    public BigDecimal getCostoReparacionVehiculo() {
        return costoReparacionVehiculo;
    }

    public BigDecimal getIngresoAcumulado() {
        return ingresoAcumulado;
    }

    public BigDecimal getGastoAcumulado() {
        return gastoAcumulado;
    }

    public int getEventosOcurridos() {
        return eventosOcurridos;
    }

    static synchronized void reiniciarParaPruebas() {
        instancia = null;
    }
}
