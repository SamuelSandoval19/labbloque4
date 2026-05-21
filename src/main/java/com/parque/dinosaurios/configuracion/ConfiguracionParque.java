package com.parque.dinosaurios.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

public final class ConfiguracionParque {

    private static final String ARCHIVO_CONFIGURACION = "configuracion.properties";
    private static ConfiguracionParque instancia;

    private final Properties propiedades;

    private ConfiguracionParque() {
        this.propiedades = new Properties();
        cargarArchivo();
    }

    public static ConfiguracionParque obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionParque();
        }
        return instancia;
    }

    private void cargarArchivo() {
        try (InputStream entrada = getClass().getClassLoader()
                .getResourceAsStream(ARCHIVO_CONFIGURACION)) {

            if (entrada == null) {
                throw new IllegalStateException(
                        "No se encontro el archivo " + ARCHIVO_CONFIGURACION + " en el classpath");
            }
            propiedades.load(entrada);
        } catch (IOException ex) {
            throw new IllegalStateException("Error al leer la configuracion del parque", ex);
        }
    }

    public String obtenerTexto(String clave) {
        String valor = propiedades.getProperty(clave);
        if (valor == null) {
            throw new IllegalArgumentException("La clave de configuracion no existe: " + clave);
        }
        return valor.trim();
    }

    public int obtenerEntero(String clave) {
        return Integer.parseInt(obtenerTexto(clave));
    }

    public double obtenerDecimal(String clave) {
        return Double.parseDouble(obtenerTexto(clave));
    }

    public BigDecimal obtenerMonto(String clave) {
        return new BigDecimal(obtenerTexto(clave));
    }

    public boolean contiene(String clave) {
        return propiedades.containsKey(clave);
    }

    static void reiniciarParaPruebas() {
        instancia = null;
    }
}
