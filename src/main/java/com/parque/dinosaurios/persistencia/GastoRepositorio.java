package com.parque.dinosaurios.persistencia;

import com.parque.dinosaurios.modelo.enumeraciones.TipoGasto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GastoRepositorio {

    private static final String INSERTAR =
            "INSERT INTO gasto (tipo, descripcion, monto) VALUES (?, ?, ?)";

    private static final String SUMAR_TOTAL =
            "SELECT COALESCE(SUM(monto), 0) AS total FROM gasto";

    private static final String LISTAR =
            "SELECT id, tipo, descripcion, monto, fecha_registro FROM gasto ORDER BY id";

    private final ConexionBaseDatos conexion;

    public GastoRepositorio(ConexionBaseDatos conexion) {
        this.conexion = conexion;
    }

    public void guardar(TipoGasto tipo, String descripcion, BigDecimal monto) {
        if (!conexion.estaDisponible()) {
            return;
        }
        try (Connection con = conexion.abrirConexion();
             PreparedStatement sentencia = con.prepareStatement(INSERTAR)) {

            sentencia.setString(1, tipo.name());
            sentencia.setString(2, descripcion);
            sentencia.setBigDecimal(3, monto);
            sentencia.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Aviso: no se guardo el gasto: " + ex.getMessage());
        }
    }

    public BigDecimal sumarTotal() {
        if (!conexion.estaDisponible()) {
            return BigDecimal.ZERO;
        }
        try (Connection con = conexion.abrirConexion();
             Statement sentencia = con.createStatement();
             ResultSet resultado = sentencia.executeQuery(SUMAR_TOTAL)) {

            if (resultado.next()) {
                return resultado.getBigDecimal("total");
            }
        } catch (SQLException ex) {
            System.out.println("Aviso: no se pudo sumar gastos: " + ex.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public List<RegistroGasto> listar() {
        List<RegistroGasto> registros = new ArrayList<>();
        if (!conexion.estaDisponible()) {
            return registros;
        }
        try (Connection con = conexion.abrirConexion();
             Statement sentencia = con.createStatement();
             ResultSet resultado = sentencia.executeQuery(LISTAR)) {

            while (resultado.next()) {
                registros.add(new RegistroGasto(
                        resultado.getLong("id"),
                        resultado.getString("tipo"),
                        resultado.getString("descripcion"),
                        resultado.getBigDecimal("monto"),
                        resultado.getTimestamp("fecha_registro").toLocalDateTime()
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Aviso: no se pudo listar gastos: " + ex.getMessage());
        }
        return registros;
    }

    public record RegistroGasto(long id,
                                String tipo,
                                String descripcion,
                                BigDecimal monto,
                                LocalDateTime fechaRegistro) {
    }
}
