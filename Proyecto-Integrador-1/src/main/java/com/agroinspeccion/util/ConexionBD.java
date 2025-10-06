package com.agroinspeccion.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona las conexiones JDBC hacia la base de datos MySQL.
 */
public final class ConexionBD {

    private static final String PROPERTIES_PATH = "/db.properties";
    private static String url;
    private static String usuario;
    private static String contrasena;

    private ConexionBD() {
        // Evitar instanciación
    }

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream input = ConexionBD.class.getResourceAsStream(PROPERTIES_PATH)) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el archivo de configuración " + PROPERTIES_PATH);
            }
            propiedades.load(input);
            url = propiedades.getProperty("db.url");
            usuario = propiedades.getProperty("db.user");
            contrasena = propiedades.getProperty("db.password");
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Error al cargar la configuración de la base de datos: " + e.getMessage());
        }
    }

    /**
     * Obtiene una nueva conexión utilizando los parámetros configurados.
     *
     * @return conexión activa
     * @throws SQLException si ocurre un error al abrir la conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }
}
