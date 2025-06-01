package org.example;

import org.example.dao.DepartamentoDAO;
import org.example.dao.DepartamentoDAOImpl;
import org.example.dao.EmpleadoDAO;
import org.example.dao.EmpleadoDAOImpl;
import org.example.model.Departamento;
import org.example.model.Empleado;
import org.example.util.DatabaseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class Main {

    // Declaramos el logger para la clase Main
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // Inicializar la base de datos
        DatabaseUtil.initDatabase();
        logger.info("Base de datos inicializada correctamente");

        // Instanciar DAO
        DepartamentoDAO departamentoDAO = new DepartamentoDAOImpl();
        EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

        logger.info("=== DEMOSTRACION DE OPERACIONES CRUD CON DAO GENERICO ===");

        logger.info("=== CREAR DEPARTAMENTOS ===");
        Departamento deptRH = new Departamento("Recursos Humanos");
        int deptRHId = departamentoDAO.crear(deptRH);
        logger.info("Departamento creado: {} - ID: {}", deptRH.getNombre(), deptRHId);

        Departamento deptIT = new Departamento("Tecnologia");
        int deptITId = departamentoDAO.crear(deptIT);
        logger.info("Departamento creado: {} - ID: {}", deptIT.getNombre(), deptITId);

        Departamento deptVentas = new Departamento("Ventas");
        int deptVentasId = departamentoDAO.crear(deptVentas);
        logger.info("Departamento creado: {} - ID: {}", deptVentas.getNombre(), deptVentasId);

        logger.info("=== CREAR EMPLEADOS ===");
        Empleado emp1 = new Empleado("Juan", "Perez", deptRHId);
        int emp1Id = empleadoDAO.crear(emp1);
        logger.info("Empleado creado: {} {} - ID: {}", emp1.getNombre(), emp1.getApellido(), emp1Id);

        Empleado emp2 = new Empleado("Maria", "Gomez", deptITId);
        int emp2Id = empleadoDAO.crear(emp2);
        logger.info("Empleado creado: {} {} - ID: {}", emp2.getNombre(), emp2.getApellido(), emp2Id);

        Empleado emp3 = new Empleado("Carlos", "Fernandez", deptVentasId);
        int emp3Id = empleadoDAO.crear(emp3);
        logger.info("Empleado creado: {} {} - ID: {}", emp3.getNombre(), emp3.getApellido(), emp3Id);

        logger.info("=== LISTAR TODOS LOS EMPLEADOS DE LA TABLA 'EMPLEADO' JUNTO CON JOIN A 'DEPARTAMENTO' ===");
        List<Empleado> empleados = empleadoDAO.listarTodos();
        for (Empleado emp : empleados) {
            logger.info(empleadoDAO.buscarPorId(emp.getId()));
        }

        logger.info("=== ACTUALIZAR DATO DE UN EMPLEADO ===");
        Empleado empAct = empleadoDAO.buscarPorId(emp1Id);
        if (empAct != null) {
            empAct.setApellido("Lopez");
            // Cambiar de departamento: de Recursos Humanos a Tecnologia
            empAct.setDepartamentoID(deptITId);
            boolean actualizado = empleadoDAO.actualizar(empAct);
            if (actualizado) {
                logger.info("Empleado actualizado: {}", empleadoDAO.buscarPorId(emp1Id));
            } else {
                logger.warn("No se pudo actualizar el empleado con id: {}", emp1Id);
            }
        } else {
            logger.warn("Empleado no encontrado para actualizar: id = {}", emp1Id);
        }

        logger.info("=== ELIMINAR UN EMPLEADO ===");
        Empleado empAeliminar = empleadoDAO.buscarPorId(2);
        if (empAeliminar != null) {
            logger.info("A eliminar: {} ", empleadoDAO.buscarPorId(empAeliminar.getId()));
            boolean eliminado = empleadoDAO.eliminar(2);
            logger.info("Empleado eliminado: {}", eliminado);
        } else {
            logger.warn("Empleado no encontrado para eliminar: id = {}", 2);
        }

        logger.info("=== LISTAR DESPUES DE LA ELIMINACION ===");
        empleados = empleadoDAO.listarTodos();
        for (Empleado emp : empleados) {
            logger.info(empleadoDAO.buscarPorId(emp.getId()));
        }

        logger.info("=== RESETEO DE LA DB PARA VOLVER A EJECUTAR ESTE PROGRAMA ===");
        DatabaseUtil.resetearBaseDeDatos();
    }
}


