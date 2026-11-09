package org.example.dao.fcsv;

import org.example.dao.ReservaDAO;
import org.example.modelo.Espacio;
import org.example.modelo.EstadoReserva;
import org.example.modelo.Profesor;
import org.example.modelo.Reserva;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservaDAOCSV implements ReservaDAO {

    private final String ruta;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReservaDAOCSV(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Reserva> leerFichero() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");

                if (datos.length >= 8) {
                    String idReserva = datos[0].trim();
                    LocalDate fecha = LocalDate.parse(datos[1].trim(), formatter);
                    LocalTime horaInicio = LocalTime.parse(datos[2].trim());
                    LocalTime horaFin = LocalTime.parse(datos[3].trim());

                    String estadoStr = datos[4].trim().toUpperCase();
                    EstadoReserva estado = EstadoReserva.Confirmado;
                    if (estadoStr.contains("CANCEL")) {
                        estado = EstadoReserva.Cancelado;
                    } else if (estadoStr.contains("FIN")) {
                        estado = EstadoReserva.Finalizado;
                    }

                    String idProfesor;
                    String idEspacio;
                    int numAsistentes;

                    if (datos.length >= 9) {
                        idProfesor = datos[6].trim();
                        idEspacio = datos[7].trim();
                        numAsistentes = Integer.parseInt(datos[8].trim());
                    } else {
                        idProfesor = datos[5].trim();
                        idEspacio = datos[6].trim();
                        numAsistentes = Integer.parseInt(datos[7].trim());
                    }

                    Profesor prof = new Profesor(idProfesor);
                    Espacio esp = new Espacio(idEspacio);

                    Reserva reserva = new Reserva(idReserva, fecha, horaInicio, horaFin, estado, prof, esp, numAsistentes);
                    reservas.add(reserva);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero CSV de reservas: " + e.getMessage());
        }
        return reservas;
    }

    @Override
    public void escribirFichero(ArrayList<Reserva> reservas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Reserva res : reservas) {
                String idProf = res.getIdProfesor() != null ? res.getIdProfesor().getIdProfesor() : "";
                String idEsp = res.getIdEspacio() != null ? res.getIdEspacio().getIdEspacio() : "";

                String estadoStr = "Confirmado";
                if (res.getEstado() == EstadoReserva.Cancelado) {
                    estadoStr = "Cancelado";
                } else if (res.getEstado() == EstadoReserva.Finalizado) {
                    estadoStr = "Finalizado";
                }

                bw.write(res.getIdReserva() + ";" +
                        res.getFecha().format(formatter) + ";" +
                        res.getHoraInicio() + ";" +
                        res.getHoraFin() + ";" +
                        estadoStr + ";" +
                        "Reserva" + ";" +
                        idProf + ";" +
                        idEsp + ";" +
                        res.getNumAsistentes());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero CSV de reservas: " + e.getMessage());
        }
    }
}