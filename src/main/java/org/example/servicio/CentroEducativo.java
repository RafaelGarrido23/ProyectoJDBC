package org.example.servicio;

import org.example.dao.DepartamentoDAO;
import org.example.dao.EspacioDAO;
import org.example.dao.ProfesorDAO;
import org.example.dao.ReservaDAO;
import org.example.dao.fcsv.DepartamentoDAOCSV;
import org.example.dao.fcsv.EspacioDAOCSV;
import org.example.dao.fcsv.ProfesorDAOCSV;
import org.example.dao.fcsv.ReservaDAOCSV;
import org.example.dao.fdat.DepartamentoDAOFB;
import org.example.dao.fdat.EspacioDAOFB;
import org.example.dao.fdat.ProfesorDAOFB;
import org.example.dao.fdat.ReservaDAOFB;
import org.example.dao.fjson.DepartamentoDAOFJS;
import org.example.dao.fjson.EspacioDAOFJS;
import org.example.dao.fjson.ProfesorDAOFJS;
import org.example.dao.fjson.ReservaDAOFJS;
import org.example.modelo.Departamento;
import org.example.modelo.Espacio;
import org.example.modelo.Profesor;
import org.example.modelo.Reserva;

import java.io.IOException;
import java.util.ArrayList;

public class CentroEducativo {

    private DepartamentoDAO departamentoDAO;
    private ProfesorDAO profesorDAO;
    private EspacioDAO espacioDAO;
    private ReservaDAO reservaDAO;

    private ArrayList<Departamento> departamentos = new ArrayList<>();
    private ArrayList<Profesor> profesores = new ArrayList<>();
    private ArrayList<Espacio> espacios = new ArrayList<>();
    private ArrayList<Reserva> reservas = new ArrayList<>();

    public CentroEducativo(DepartamentoDAOCSV departamentoDAOCSV, ProfesorDAOCSV profesorDAOCSV, EspacioDAOCSV espacioDAOCSV, ReservaDAOCSV reservaDAOCSV) throws IOException {
        this.departamentoDAO = departamentoDAOCSV;
        this.profesorDAO = profesorDAOCSV;
        this.espacioDAO = espacioDAOCSV;
        this.reservaDAO = reservaDAOCSV;
        leerTodo();
    }

    public CentroEducativo(DepartamentoDAOFB departamentoDAOFB, ProfesorDAOFB profesorDAOFB, EspacioDAOFB espacioDAOFB, ReservaDAOFB reservaDAOFB) throws IOException {
        this.departamentoDAO = departamentoDAOFB;
        this.profesorDAO = profesorDAOFB;
        this.espacioDAO = espacioDAOFB;
        this.reservaDAO = reservaDAOFB;
        leerTodo();
    }

    public CentroEducativo(DepartamentoDAOFJS departamentoDAOFJS, ProfesorDAOFJS profesorDAOFJS, EspacioDAOFJS espacioDAOFJS, ReservaDAOFJS reservaDAOFJS) throws IOException {
        this.departamentoDAO = departamentoDAOFJS;
        this.profesorDAO = profesorDAOFJS;
        this.espacioDAO = espacioDAOFJS;
        this.reservaDAO = reservaDAOFJS;
        leerTodo();
    }

    public void leerTodo() {
        departamentos = departamentoDAO.leerFichero();
        profesores = profesorDAO.leerFichero();
        espacios = espacioDAO.leerFichero();
        reservas = reservaDAO.leerFichero();

        asociarAProfesores();
        asociarAReservas();
    }

    public void guardarTodo() throws IOException{
        departamentoDAO.escribirFichero(departamentos);
        profesorDAO.escribirFichero(profesores);
        espacioDAO.escribirFichero(espacios);
        reservaDAO.escribirFichero(reservas);
    }

    public ArrayList<Departamento> getDepartamentos(){
        return departamentos;
    }

    public ArrayList<Profesor> getProfesores(){
        return profesores;
    }

    public ArrayList<Espacio> getEspacios(){
        return espacios;
    }

    public ArrayList<Reserva> getReservas(){
        return reservas;
    }

    private void asociarAProfesores() {

        for (Profesor profesor : profesores) {
            Departamento departamento = buscarDepartamentoPorId(
                    profesor.getIdDepartamento() == null
                            ? null
                            : profesor.getIdDepartamento().getIdDepartamento()
            );

            profesor.setIdDepartamento(departamento);
        }
    }

    private void asociarAReservas() {
        for (Reserva reserva : reservas) {

            if (reserva.getIdProfesor() != null) {
                String idProfesor = reserva.getIdProfesor().getIdProfesor();

                for (Profesor profesor : profesores) {
                    if (profesor.getIdProfesor().equals(idProfesor)) {
                        reserva.setIdProfesor(profesor);
                        break;
                    }
                }
            }

            if (reserva.getIdEspacio() != null) {
                String idEspacio = reserva.getIdEspacio().getIdEspacio();

                for (Espacio espacio : espacios) {
                    if (espacio.getIdEspacio().equals(idEspacio)) {
                        reserva.setIdEspacio(espacio);
                        break;
                    }
                }
            }
        }
    }

    private Departamento buscarDepartamentoPorId(String id){
        for (Departamento departamento : departamentos){
            if (departamento.getIdDepartamento().equals(id)){
                return departamento;
            }
        }
        return null;
    }
}
