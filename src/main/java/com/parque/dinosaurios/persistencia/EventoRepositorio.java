package com.parque.dinosaurios.persistencia;

import com.parque.dinosaurios.modelo.enumeraciones.TipoEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoRepositorio {

    private static final String INSERTAR =
            "INSERT INTO evento (tipo, descripcion, paso_simulacion, resuelto) "
                    + "VALUES (?, ?, ?, ?)";

    private static final String LISTAR =
            "SELECT id, tipo, descripcion, paso_simulacion, resuelto, fecha_registro "
                    + "FROM evento ORDER BY id";

    private static final String CONTAR_POR_TIPO =
            "SELECT tipo, COUNT(*) AS cantidad FROM evento GROUP BY tipo";

    private final ConexionBaseDatos conexion;

    public EventoRepositorio(ConexionBaseDatos conexion) {
        this.conexion = conexion;
    }

    public void guardar(TipoEvento tipo, String descripcion, int pasoSimulacion, boolean resuelto) {
        if (!conexion.estaDisponible()) {
            return;
        }
        try (Connection con = conexion.abrirConexion();
             PreparedStatement sentencia = con.prepareStatement(INSERTAR)) {

            sentencia.setString(1, tipo.name());
            sentencia.setString(2, descripcion);
            sentencia.setInt(3, pasoSimulacion);
            sentencia.setBoolean(4, resuelto);
            sentencia.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Aviso: no se guardo el evento: " + ex.getMessage());
        }
    }

    public List<RegistroEvento> listar() {
        List<RegistroEvento> registros = new ArrayList<>();
        if (!conexion.estaDisponible()) {
            return registros;
        }
        try (Connection con = conexion.abrirConexion();
             Statement sentencia = con.createStatement();
             ResultSet resultado = sentencia.executeQuery(LISTAR)) {

            while (resultado.next()) {
                registros.add(new RegistroEvento(
                        resultado.getLong("id"),
                        resultado.getString("tipo"),
                        resultado.getString("descripcion"),
                        resultado.getInt("paso_simulacion"),
                        resultado.getBoolean("resuelto"),
                        resultado.getTimestamp("fecha_registro").toLocalDateTime()
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Aviso: no se pudo listar eventos: " + ex.getMessage());
        }
        return registros;
    }

    public List<ConteoEvento> contarPorTipo() {
        List<ConteoEvento> conteos = new ArrayList<>();
        if (!conexion.estaDisponible()) {
            return conteos;
        }
        try (Connection con = conexion.abrirConexion();
             Statement sentencia = con.createStatement();
             ResultSet resultado = sentencia.executeQuery(CONTAR_POR_TIPO)) {

            while (resultado.next()) {
                conteos.add(new ConteoEvento(
                        resultado.getString("tipo"),
                        resultado.getInt("cantidad")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Aviso: no se pudo contar eventos: " + ex.getMessage());
        }
        return conteos;
    }

    public record RegistroEvento(long id,
                                 String tipo,
                                 String descripcion,
                                 int pasoSimulacion,
                                 boolean resuelto,
                                 LocalDateTime fechaRegistro) {
    }

    public record ConteoEvento(String tipo, int cantidad) {
    }
}
