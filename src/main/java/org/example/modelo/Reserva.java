package org.example.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private String idReserva;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoReserva estado;
    private Profesor idProfesor;
    private Espacio idEspacio;
    private int numAsistentes;

    public Reserva() {
    }

    public Reserva(String idReserva, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, EstadoReserva estado,
                   Profesor idProfesor, Espacio idEspacio, int numAsistentes) {
        this.idReserva = idReserva;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.idProfesor = idProfesor;
        this.idEspacio = idEspacio;
        this.numAsistentes = numAsistentes;
    }

    // Getter & Setter "ID 'Reserva'"
    public String getIdReserva() { return idReserva; }
    public void setIdReserva(String id) { this.idReserva = id; }

    // Getter & Setter "Fecha 'Reserva'"
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    // Getter & Setter "horaInicio 'Reserva'"
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    // Getter & Setter "horaFin 'Reserva'"
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    // Getter & Setter "Estado 'Reserva'"
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    // Getter & Setter "Profesor 'Reserva'"
    public Profesor getIdProfesor() { return idProfesor; }
    public void setIdProfesor(Profesor idProfesor) { this.idProfesor = idProfesor; }

    // Getter & Setter "Espacio 'Reserva'"
    public Espacio getIdEspacio() { return idEspacio; }
    public void setIdEspacio(Espacio idEspacio) { this.idEspacio = idEspacio; }

    // Getter & Setter "numAsistentes 'Reserva'"
    public int getNumAsistentes() { return numAsistentes; }
    public void setNumAsistentes(int numAsistentes) { this.numAsistentes = numAsistentes; }

    @Override
    public String toString() {
        return "ID Reserva: " + idReserva +
                " | Fecha: " + fecha +
                " | Hora Inicio: " + horaInicio +
                " | Hora Fin: " + horaFin +
                " | Estado: " + estado +
                " | Profesor: " + idProfesor +
                " | Espacio: " + idEspacio +
                " | NºAsistentes: " + numAsistentes;
    }
}
