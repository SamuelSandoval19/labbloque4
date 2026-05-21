package com.parque.dinosaurios.zonas;

import com.parque.dinosaurios.modelo.Dinosaurio;
import com.parque.dinosaurios.modelo.Turista;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecintoObservacion extends Zona {

    private final String tipoExperiencia;
    private final BigDecimal costoEntrada;
    private final List<Dinosaurio> dinosauriosExhibidos;
    private final List<Integer> calificacionesEncuesta;
    private final Random aleatorio;
    private int ultimaCalificacionBase;

    public RecintoObservacion(String tipoExperiencia,
                              int capacidadMaxima,
                              BigDecimal costoEntrada,
                              long semilla) {
        super("Recinto " + tipoExperiencia, capacidadMaxima);
        this.tipoExperiencia = tipoExperiencia;
        this.costoEntrada = costoEntrada;
        this.dinosauriosExhibidos = new ArrayList<>();
        this.calificacionesEncuesta = new ArrayList<>();
        this.aleatorio = new Random(semilla);
        this.ultimaCalificacionBase = 7;
    }

    public void agregarDinosaurio(Dinosaurio dinosaurio) {
        dinosauriosExhibidos.add(dinosaurio);
    }

    public boolean cobrarEntrada(Turista turista) {
        if (!turista.puedePagar(costoEntrada)) {
            return false;
        }
        turista.cobrar(costoEntrada);
        return true;
    }

    public int aplicarEncuesta() {
        int variacion = aleatorio.nextInt(3) - 1;
        int calificacion = Math.max(1, Math.min(10, ultimaCalificacionBase + variacion));
        ultimaCalificacionBase = calificacion;
        calificacionesEncuesta.add(calificacion);
        return calificacion;
    }

    public double obtenerPromedioSatisfaccion() {
        if (calificacionesEncuesta.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (int c : calificacionesEncuesta) {
            suma += c;
        }
        return suma / calificacionesEncuesta.size();
    }

    public String getTipoExperiencia() {
        return tipoExperiencia;
    }

    public BigDecimal getCostoEntrada() {
        return costoEntrada;
    }

    public List<Dinosaurio> getDinosauriosExhibidos() {
        return dinosauriosExhibidos;
    }

    public int getEncuestasAplicadas() {
        return calificacionesEncuesta.size();
    }

    @Override
    public String describirActividad() {
        return "Experiencia de observacion (" + tipoExperiencia + ") con encuesta de satisfaccion correlacionada";
    }
}
