package org.example.dao;

import org.example.model.Departamento;
import org.example.util.DatabaseUtil;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartamentoDAOImpl extends GenericDAO<Departamento> implements DepartamentoDAO {

    // Instancia del logger con Log4j2
    private static final Logger logger = LogManager.getLogger(DepartamentoDAOImpl.class);

    @Override
    protected Departamento mapRow(ResultSet rs) throws SQLException {
        return new Departamento(rs.getInt("id"), rs.getString("nombre"));
    }

    @Override
    protected String getTableName() {
        return "departamentos";
    }

    @Override
    public int crear(Departamento departamento) {
        String sql = "INSERT INTO departamentos (nombre) VALUES (?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departamento.getNombre());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error al crear departamento", e);
        }
        return -1;
    }

    @Override
    public boolean actualizar(Departamento departamento) {
        String sql = "UPDATE departamentos SET nombre = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departamento.getNombre());
            stmt.setInt(2, departamento.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error al actualizar departamento con id=" + departamento.getId(), e);
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM departamentos WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error al eliminar departamento con id=" + id, e);
        }
        return false;
    }

    // Los metodos listarTodos() y buscarPorId() se heredan de GenericDAO.
}

