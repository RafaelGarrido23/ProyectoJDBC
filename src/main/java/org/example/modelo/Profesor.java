package org.example.modelo;

public class Profesor {

    private String idProfesor;
    private String nombre;
    private String apellidos;
    private String email;
    private Departamento idDepartamento;

    public Profesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Profesor(String idProfesor, String nombre, String apellidos, String email, Departamento idDepartamento) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.idDepartamento = idDepartamento;
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
    public Departamento getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(Departamento idDepartamento) { this.idDepartamento = idDepartamento; }

    @Override
    public String toString() {
        return "ID Profesor: " + idProfesor +
                " | Nombre: " + nombre +
                " | Apellidos: " + apellidos +
                " | Email: " + email +
                " | Departamento: " + idDepartamento;
    }
}
