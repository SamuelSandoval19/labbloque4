package com.parque.dinosaurios.persistencia;

import com.parque.dinosaurios.configuracion.ConfiguracionParque;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {

    private static final String CHANGELOG = "db/changelog/db.changelog-master.xml";

    private final String url;
    private final String usuario;
    private final String contrasena;
    private final String driver;
    private boolean disponible;
    private boolean migracionEjecutada;

    public ConexionBaseDatos() {
        ConfiguracionParque cfg = ConfiguracionParque.obtenerInstancia();
        this.url = cfg.obtenerTexto("db.url");
        this.usuario = cfg.obtenerTexto("db.usuario");
        this.contrasena = cfg.obtenerTexto("db.contrasena");
        this.driver = cfg.obtenerTexto("db.driver");
        this.disponible = false;
        this.migracionEjecutada = false;
    }

    public ConexionBaseDatos(String url, String usuario, String contrasena, String driver) {
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.driver = driver;
        this.disponible = false;
        this.migracionEjecutada = false;
    }

    public boolean intentarConectar() {
        try {
            Class.forName(driver);
            try (Connection conexion = abrirConexion()) {
                disponible = conexion != null && !conexion.isClosed();
            }
            return disponible;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Aviso: no se pudo conectar a la base de datos. "
                    + "La simulacion correra sin persistencia. Detalle: " + ex.getMessage());
            disponible = false;
            return false;
        }
    }

    public void ejecutarMigraciones() {
        if (!disponible) {
            return;
        }
        if (migracionEjecutada) {
            return;
        }
        try (Connection conexion = abrirConexion()) {
            Database baseDatos = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conexion));

            try (Liquibase liquibase = new Liquibase(CHANGELOG,
                    new ClassLoaderResourceAccessor(), baseDatos)) {
                liquibase.update("");
            }
            migracionEjecutada = true;
            System.out.println("Migraciones de Liquibase aplicadas correctamente.");
        } catch (Exception ex) {
            System.out.println("Aviso: fallo la migracion de Liquibase: " + ex.getMessage());
        }
    }

    public Connection abrirConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    public boolean estaDisponible() {
        return disponible;
    }
}
