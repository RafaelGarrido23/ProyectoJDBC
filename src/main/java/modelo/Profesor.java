package modelo;

import java.io.Serializable;

public class Profesor implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idProfesor;
    private String nombre;
    private String apellidos;
    private String email;
    private Departamento departamento;

    public Profesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Profesor(String idProfesor, String nombre, String apellidos, String email, Departamento departamento) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.departamento = departamento;
    }

    public Profesor() {
    }

    // Getter & Setter "ID 'Profesor'"
    public String getIdProfesor() { return idProfesor; }
    public void setIdProfesor(String id) { this.idProfesor = id; }

    // Getter & Setter "Nombre 'Profesor'"
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Getter & Setter "Apellidos 'Profesor'"
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    // Getter & Setter "Email 'Profesor'"
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Getter & Setter "Departamento 'Profesor'"
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    @Override
    public String toString() {
        return "ID Profesor: " + idProfesor +
                " | Nombre: " + nombre +
                " | Apellidos: " + apellidos +
                " | Email: " + email +
                " | Departamento: " + departamento;
    }
}
