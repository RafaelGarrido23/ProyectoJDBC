package modelo;

import java.io.Serializable;

public class Espacio implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idEspacio;
    private String nombre;
    private String tipo;
    private String ubicacion;
    private int capacidad;
    private String equipamiento;
    private String disponible;

    public Espacio(String idEspacio) {
        this.idEspacio = idEspacio;
    }

    public Espacio(String idEspacio, String nombre, String tipo, String ubicacion, int capacidad, String equipamiento, String disponible) {
        this.idEspacio = idEspacio;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.equipamiento = equipamiento;
        this.disponible = disponible;
    }

    // Getter & Setter "ID 'Espacio'"
    public String getIdEspacio() { return idEspacio; }
    public void setIdEspacio(String id) { this.idEspacio = id; }

    // Getter & Setter "Nombre 'Espacio'"
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Getter & Setter "Tipo 'Espacio'"
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Getter & Setter "Ubicación 'Espacio'"
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    // Getter & Setter "Equipamiento 'Espacio'"
    public String getEquipamiento() { return equipamiento; }
    public void setEquipamiento(String equipamiento) { this.equipamiento = equipamiento; }

    // Getter & Setter "Capacidad 'Espacio'"
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    // Getter & Setter "Disponibilidad 'Espacio'"
    public String getDisponible() { return disponible; }
    public void setDisponible(String disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "ID Espacio: " + idEspacio +
                " | Nombre: " + nombre +
                " | Tipo: " + tipo +
                " | Ubicación: " + ubicacion +
                " | Capacidad: " + capacidad +
                " | Disponibilidad: " + disponible;
    }
}
