package com.parque.dinosaurios.monitoreo;

import com.parque.dinosaurios.eventos.ApagonMasivo;
import com.parque.dinosaurios.eventos.EventoAleatorio;
import com.parque.dinosaurios.parque.AyudanteParque;
import com.parque.dinosaurios.parque.Parque;
import com.parque.dinosaurios.persistencia.EventoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MonitoreoTest {

    private Parque parque;

    @BeforeEach
    void inicializar() {
        parque = AyudanteParque.construirParqueLimpio();
    }

    @Test
    void monitorConsolaImprimeReporte() {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(salida));
        try {
            MonitorConsola monitor = new MonitorConsola();
            ReporteEstado reporte = parque.construirReporte(3);
            monitor.recibirReporte(reporte);
        } finally {
            System.setOut(original);
        }
        String texto = salida.toString();
        assertTrue(texto.contains("Paso"));
        assertTrue(texto.contains("Turistas"));
    }

    @Test
void monitorConsolaImprimeEvento() {
    ByteArrayOutputStream salida = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(salida));
    try {
        MonitorConsola monitor = new MonitorConsola();
        EventoAleatorio evento = new ApagonMasivo();
        evento.ejecutar(parque);
        monitor.notificarEvento(evento, 5);
    } finally {
        System.setOut(original);
    }
    String texto = salida.toString();
    assertTrue(texto.contains("APAGON") || texto.contains("Apagon") || texto.contains("evento"));
}

    @Test
    void registroEventosUsaElRepositorio() {
        EventoRepositorio repoMock = mock(EventoRepositorio.class);
        RegistroEventos registro = new RegistroEventos(repoMock);

        EventoAleatorio evento = new ApagonMasivo();
        evento.ejecutar(parque);
        registro.notificarEvento(evento, 9);

        verify(repoMock, times(1))
                .guardar(eq(evento.getTipo()), anyString(), eq(9), eq(true));
        assertEquals(1, registro.getTotalRegistrados());
    }

    @Test
    void sujetoObservableNotificaATodosLosObservadores() {
        AtomicInteger conteo = new AtomicInteger();
        Observador uno = new Observador() {
            @Override public void recibirReporte(ReporteEstado r) { conteo.incrementAndGet(); }
        };
        Observador dos = new Observador() {
            @Override public void recibirReporte(ReporteEstado r) { conteo.incrementAndGet(); }
        };
        parque.agregarObservador(uno);
        parque.agregarObservador(dos);
        parque.publicarReporte(1);
        assertEquals(2, conteo.get());
    }

    @Test
    void agregarObservadorDosVecesNoDuplica() {
        Observador o = new MonitorConsola();
        int antes = parque.cantidadObservadores();
        parque.agregarObservador(o);
        parque.agregarObservador(o);
        assertEquals(antes + 1, parque.cantidadObservadores());
    }

    @Test
    void reporteEstadoConservaValores() {
        ReporteEstado r = new ReporteEstado(
                5, 10, 3, 8, 700, 70, 2, 1, 4,
                new BigDecimal("1000"), new BigDecimal("500"));
        assertEquals(5, r.getPaso());
        assertEquals(10, r.getTuristasActivos());
        assertEquals(3, r.getTuristasEnFila());
        assertEquals(8, r.getDinosauriosEnRecintos());
        assertEquals(700, r.getEnergiaDisponible());
        assertEquals(70, r.getPorcentajeEnergia());
        assertEquals(2, r.getEventosOcurridos());
        assertEquals(1, r.getVehiculosEnUso());
        assertEquals(4, r.getVehiculosOperativos());
        assertEquals(new BigDecimal("1000"), r.getIngresoAcumulado());
        assertEquals(new BigDecimal("500"), r.getGastoAcumulado());
    }
}
