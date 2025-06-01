package org.example.model;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private int departamentoID;

    private String departamentoNombre;

    // Constructor vacío
    public Empleado() {
    }

    // Constructor sin ID (para nuevos Empleados)
    public Empleado(String nombre, String apellido, int departamentoID) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.departamentoID = departamentoID;
    }

    // Constructor completo
    public Empleado(int id, String nombre, String apellido, int departamentoID) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.departamentoID = departamentoID;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDepartamentoID() {
        return departamentoID;
    }

    public void setDepartamentoID(int departamentoID) {
        this.departamentoID = departamentoID;
    }

    public String getDepartamentoNombre() {
        return departamentoNombre;
    }

    public void setDepartamentoNombre(String departamentoNombre) {
        this.departamentoNombre = departamentoNombre;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", departamentoID='" + departamentoID +
                ", departamentoNombre='" + departamentoNombre + '\'' +
                '}';
    }
}
