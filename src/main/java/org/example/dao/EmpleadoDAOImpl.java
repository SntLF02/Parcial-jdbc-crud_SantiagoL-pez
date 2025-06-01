package org.example.dao;

import org.example.model.Empleado;
import org.example.util.DatabaseUtil;
import java.sql.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpleadoDAOImpl extends GenericDAO<Empleado> implements EmpleadoDAO {

    // Declaracion del logger con Log4j2
    private static final Logger logger = LogManager.getLogger(EmpleadoDAOImpl.class);

    @Override
    protected Empleado mapRow(ResultSet rs) throws SQLException {

        // Se asume que la consulta pueda incluir el nombre del departamento.
        Empleado empleado = new Empleado(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getInt("departamentoID")
        );
        try {
            // Se intenta asignar el nombre del departamento, por si la consulta lo trae (por ejemplo, con JOIN)
            empleado.setDepartamentoNombre(rs.getString("departamentoNombre"));
        } catch (SQLException ex) {
            // Se registra un aviso (warn) indicando que no se pudo obtener el nombre
            logger.warn("No se pudo obtener 'departamentoNombre' del ResultSet", ex);
        }
        return empleado;
    }

    @Override
    protected String getTableName() {
        return "empleados";
    }

    @Override
    public int crear(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, apellido, departamentoID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setInt(3, empleado.getDepartamentoID());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            // Uso de logger para registrar el error
            logger.error("Error al crear empleado", e);
        }
        return -1;
    }

    @Override
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, departamentoID = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setInt(3, empleado.getDepartamentoID());
            stmt.setInt(4, empleado.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error al actualizar empleado con id=" + empleado.getId(), e);
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error al eliminar empleado con id=" + id, e);
        }
        return false;
    }

    @Override
    public Empleado buscarPorId(int id) {
        String sql = "SELECT e.id, e.nombre, e.apellido, e.departamentoID, d.nombre AS departamentoNombre " +
                "FROM empleados e " +
                "JOIN departamentos d ON e.departamentoID = d.id " +
                "WHERE e.id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error en buscarPorId para el empleado con id=" + id, e);
        }
        return null;
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();

        // Consulta que incluye el JOIN para traer "departamentoNombre"
        String sql = "SELECT e.id, e.nombre, e.apellido, e.departamentoID, d.nombre AS departamentoNombre " +
                "FROM empleados e " +
                "JOIN departamentos d ON e.departamentoID = d.id";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapRow(rs)); // mapRow establece departamentoNombre
            }
        } catch (SQLException e) {
            logger.error("Error en listarTodos de Empleados", e);
        }
        return lista;
    }
}
