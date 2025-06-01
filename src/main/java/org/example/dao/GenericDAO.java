package org.example.dao;

import org.example.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GenericDAO<T> {

    // Declaracion del logger para esta clase
    private static final Logger logger = LogManager.getLogger(GenericDAO.class);

    // Metodo abstracto para mapear una fila del ResultSet en una instancia de T
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    // Metodo abstracto para obtener el nombre de la tabla en la base de datos
    protected abstract String getTableName();

    // Metodo generico para listar todos los registros
    public List<T> listarTodos() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al listar todos los registros de la tabla: " + getTableName(), e);
        }
        return list;
    }

    // Metodo generico para buscar un registro por su ID
    public T buscarPorId(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar registro con id: " + id + " en la tabla: " + getTableName(), e);
        }
        return null;
    }

    // Metodos abstractos para crear, actualizar y eliminar
    public abstract int crear(T entity);
    public abstract boolean actualizar(T entity);
    public abstract boolean eliminar(int id);
}
