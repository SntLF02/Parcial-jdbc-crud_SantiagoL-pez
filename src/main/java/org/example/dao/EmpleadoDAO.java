package org.example.dao;

import org.example.model.Empleado;
import java.util.List;

public interface EmpleadoDAO {
    int crear(Empleado empleado);
    Empleado buscarPorId(int id);
    List<Empleado> listarTodos();
    boolean actualizar(Empleado empleado);
    boolean eliminar(int id);
}
