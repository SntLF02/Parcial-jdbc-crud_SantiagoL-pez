package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseUtil {
    // Declaracion del logger
    private static final Logger logger = LogManager.getLogger(DatabaseUtil.class);

    private static final String DB_URL = "jdbc:h2:./data/Empresadb";
    private static final String DB_USER = "santi";
    private static final String DB_PASSWORD = "";

    // Metodo para obtener una conexion
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Metodo para cerrar recursos
    public static void closeResources(Connection conn, Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            logger.error("Error al cerrar Statement", e);
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.error("Error al cerrar Connection", e);
        }
    }

    // Metodo para inicializar la base de datos
    public static void initDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // Crear tabla Departamentos si no existe
            String sqlDepartamentos = "CREATE TABLE IF NOT EXISTS Departamentos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL)";

            stmt.execute(sqlDepartamentos);

            // Crear tabla Empleados si no existe
            String sqlEmpleados = "CREATE TABLE IF NOT EXISTS Empleados (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "apellido VARCHAR(100) NOT NULL, " +
                    "departamentoID INT, " +
                    "FOREIGN KEY (departamentoID) REFERENCES Departamentos(id))";

            stmt.execute(sqlEmpleados);

            logger.info("Base de datos inicializada correctamente");

        } catch (SQLException e) {
            logger.error("Error al inicializar la base de datos", e);
        } finally {
            closeResources(conn, stmt);
        }
    }

    public static void resetearBaseDeDatos() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("=== RESETEANDO LA BASE DE DATOS ===");

            // Limpiar las tablas sin afectar las restricciones de claves foraneas
            stmt.executeUpdate("DELETE FROM Empleados;");
            stmt.executeUpdate("DELETE FROM Departamentos;");

            // Reiniciar el contador de ID (AUTO_INCREMENT)
            stmt.executeUpdate("ALTER TABLE Empleados ALTER COLUMN id RESTART WITH 1;");
            stmt.executeUpdate("ALTER TABLE Departamentos ALTER COLUMN id RESTART WITH 1;");

            logger.info("Base de datos reiniciada correctamente.");

        } catch (SQLException e) {
            logger.error("Error al resetear la base de datos", e);
        }
    }
}

