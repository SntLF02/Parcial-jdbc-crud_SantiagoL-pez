package org.example.dao;

import org.example.model.Departamento;
import java.util.List;

public interface DepartamentoDAO {
    int crear(Departamento departamento);
    Departamento buscarPorId(int id);
    List<Departamento> listarTodos();
    boolean actualizar(Departamento departamento);
    boolean eliminar(int id);
}
