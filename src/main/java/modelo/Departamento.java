package modelo;

import java.io.Serializable;

public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idDepartamento;
    private String nombre;

    public Departamento() {
    }

    public Departamento(String idDepartamento, String nombre) {
        this.idDepartamento = idDepartamento;
        this.nombre = nombre;
    }

    public Departamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    // Getter & Setter "ID 'Departamento'"
    public String getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(String id) { this.idDepartamento = id; }

    // Getter & Setter "Nombre 'Departamento'"
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return this.idDepartamento;
    }
}
