package servicio;

import dao.DepartamentoDAO;
import dao.EspacioDAO;
import dao.ProfesorDAO;
import dao.ReservaDAO;
import dao.fdat.DepartamentoDAOFB;
import dao.fdat.EspacioDAOFB;
import dao.fdat.ProfesorDAOFB;
import dao.fdat.ReservaDAOFB;
import dao.fjson.DepartamentoDAOFJS;
import dao.fjson.EspacioDAOFJS;
import dao.fjson.ProfesorDAOFJS;
import dao.fjson.ReservaDAOFJS;
import modelo.*;
import dao.fcsv.DepartamentoDAOCSV;
import dao.fcsv.EspacioDAOCSV;
import dao.fcsv.ProfesorDAOCSV;
import dao.fcsv.ReservaDAOCSV;

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
                profesor.getDepartamento() == null
                        ? null
                        : profesor.getDepartamento().getIdDepartamento()
            );

            profesor.setDepartamento(departamento);
        }
    }

    private void asociarAReservas() {
        for (Reserva reserva : reservas) {

            if (reserva.getProfesor() != null) {
                String idProfesor = reserva.getProfesor().getIdProfesor();

                for (Profesor profesor : profesores) {
                    if (profesor.getIdProfesor().equals(idProfesor)) {
                        reserva.setProfesor(profesor);
                        break;
                    }
                }
            }

            if (reserva.getEspacio() != null) {
                String idEspacio = reserva.getEspacio().getIdEspacio();

                for (Espacio espacio : espacios) {
                    if (espacio.getIdEspacio().equals(idEspacio)) {
                        reserva.setEspacio(espacio);
                        break;
                    }
                }
            }
        }
    }



    //***********************************************
    //
    // MÉTODOS DE GESTIÓN DE DEPARTAMENTOS
    //
    //***********************************************

    public void altaDepartamento(Departamento departamento){
        validarDepartamento(departamento);

        if ((buscarDepartamentoPorId(departamento.getIdDepartamento()) != null)){
            throw new IllegalArgumentException("Ya existe un departamento con ese ID");
        }

        departamentos.add(departamento);
    }

    public void modificarDepartamento(Departamento modificado){
        validarDepartamento(modificado);

        Departamento departamento = buscarDepartamentoPorId(modificado.getIdDepartamento());
        departamento.setNombre(departamento.getNombre());
    }

    public void bajaDepartamento(String id){

        if (id == null) {
            throw new IllegalArgumentException("El ID del departamento no puede ser nulo.");
        }

        String idLimpio = id.trim();

        // Verificar si existen profesores en el departamento
        boolean tieneProfesores = profesores.stream()
                .anyMatch(profesor -> {
                    String idProfDept = profesor.getDepartamento().getIdDepartamento();
                    return idProfDept != null && idProfDept.trim().equalsIgnoreCase(idLimpio);
                });

        // Lanzar la excepción si hay profesores vinculados
        if (tieneProfesores) {
            throw new IllegalArgumentException("No se puede dar de baja: El departamento seleccionado tiene profesores asignados.");
        }

        // Proceder con la eliminación si la comprobación anterior fue exitosa
        Departamento departamento = buscarDepartamentoPorId(idLimpio);
        if (departamento != null) {
            departamentos.remove(departamento);
        } else {
            throw new IllegalArgumentException("El departamento con ID '" + idLimpio + "' no existe.");
        }
    }

    public ArrayList<Departamento> consultarDepartamentos(){
        return new ArrayList<>(departamentos);
    }

    public Departamento consultarDepartamento(String id){
        Departamento departamento = buscarDepartamentoPorId(id);

        return departamento;
    }

    private void validarDepartamento(Departamento departamento) {
        if (departamento == null){ throw new IllegalArgumentException("El espacio puede ser nulo"); }
        if (departamento.getIdDepartamento() == null || departamento.getIdDepartamento().trim().isEmpty()){ throw new IllegalArgumentException("El ID es obligatorio"); }
        if (departamento.getNombre() == null || departamento.getNombre().trim().isEmpty()){ throw new IllegalArgumentException("El nombre es obligatorio"); }
    }

    private Departamento buscarDepartamentoPorId(String id){
        for (Departamento departamento : departamentos){
            if (departamento.getIdDepartamento().equals(id)){
                return departamento;
            }
        }
        return null;
    }



    //***********************************************
    //
    // MÉTODOS DE GESTIÓN DE PROFESORES
    //
    //***********************************************

    public void altaProfesor(Profesor profesor){
        validarProfesor(profesor);

        if ((buscarProfesorPorId(profesor.getIdProfesor()) != null)){
            throw new IllegalArgumentException("Ya existe un profesor con ese ID");
        }

        profesores.add(profesor);
    }

    public void modificarProfesor(Profesor modificado){
        validarProfesor(modificado);

        Profesor profesor = buscarProfesorPorId(modificado.getIdProfesor());

        profesor.setNombre(profesor.getNombre());
        profesor.setApellidos(profesor.getApellidos());
        profesor.setEmail(profesor.getEmail());
        profesor.setDepartamento(profesor.getDepartamento());
    }

    public void bajaProfesor(String id){
        Profesor profesor = buscarProfesorPorId(id);

        profesores.remove(profesor);
    }

    public ArrayList<Profesor> consultarProfesores(){
        return new ArrayList<>(profesores);
    }

    public Profesor consultarProfesor(String id){
        Profesor profesor = buscarProfesorPorId(id);

        return profesor;
    }

    public ArrayList<Profesor> consultarPorDepartamento(String id){
        ArrayList<Profesor> resultado = new ArrayList<>();

        for (Profesor profesor : profesores){
            if (profesor.getDepartamento() != null &&
                    profesor.getDepartamento().getIdDepartamento() != null &&
                    profesor.getDepartamento().getIdDepartamento().equals(id)){
                resultado.add(profesor);
            }
        }
        return resultado;
    }

    private void validarProfesor(Profesor profesor) {
        if (profesor == null){ throw new IllegalArgumentException("El profesor puede ser nulo"); }
        if (profesor.getIdProfesor() == null || profesor.getIdProfesor().trim().isEmpty()){ throw new IllegalArgumentException("El ID es obligatorio"); }
        if (profesor.getNombre() == null || profesor.getNombre().trim().isEmpty()){ throw new IllegalArgumentException("El nombre es obligatorio"); }
        if (profesor.getApellidos() == null || profesor.getApellidos().trim().isEmpty()){ throw new IllegalArgumentException("Los apellidos son obligatorios"); }
        if (profesor.getEmail() == null || profesor.getEmail().trim().isEmpty()){ throw new IllegalArgumentException("El email es obligatorio"); }
        if (profesor.getDepartamento() == null){ throw new IllegalArgumentException("El profesor debe pertenecer a un departamento"); }
    }

    private Profesor buscarProfesorPorId(String id){
        for (Profesor profesor : profesores){
            if (profesor.getIdProfesor().equals(id)){
                return profesor;
            }
        }
        return null;
    }



    //***********************************************
    //
    // MÉTODOS DE GESTIÓN DE ESPACIOS
    //
    //***********************************************


    public void altaEspacio(Espacio espacio){
        validarEspacio(espacio);

        if ((buscarEspacioPorId(espacio.getIdEspacio()) != null)){
            throw new IllegalArgumentException("Ya existe un espacio con ese ID");
        }

        espacios.add(espacio);
    }

    public void modificarEspacio(Espacio modificado){
        validarEspacio(modificado);

        Espacio espacio = buscarEspacioPorId(modificado.getIdEspacio());

        espacio.setNombre(espacio.getNombre());
        espacio.setTipo(espacio.getTipo());
        espacio.setCapacidad(espacio.getCapacidad());
    }

    public void bajaEspacio(String id){
        Espacio espacio = buscarEspacioPorId(id);

        espacios.remove(espacio);
    }

    public ArrayList<Espacio> consultarEspacios(){
        return new ArrayList<>(espacios);
    }

    public Espacio consultarEspacio(String id){
        Espacio espacio = buscarEspacioPorId(id);

        return espacio;
    }

    public void cambiarDisponibilidad(String id){
        Espacio espacio = buscarEspacioPorId(id);

        if (espacio.getDisponible().equals("DISPONIBLE")) espacio.setDisponible("NO_DISPONIBLE");
        else espacio.setDisponible("DISPONIBLE");
    }

    private void validarEspacio(Espacio espacio) {
        if (espacio == null){ throw new IllegalArgumentException("El espacio puede ser nulo"); }
        if (espacio.getIdEspacio() == null || espacio.getIdEspacio().trim().isEmpty()){ throw new IllegalArgumentException("El ID es obligatorio"); }
        if (espacio.getNombre() == null || espacio.getNombre().trim().isEmpty()){ throw new IllegalArgumentException("El nombre es obligatorio"); }
        if (espacio.getTipo() == null || espacio.getTipo().trim().isEmpty()){ throw new IllegalArgumentException("El tipo es obligatorio"); }
        if (espacio.getCapacidad() <= 0){ throw new IllegalArgumentException("La capacidad debe ser mayor que 0"); }
    }

    private Espacio buscarEspacioPorId(String id){
        for (Espacio espacio : espacios){
            if (espacio.getIdEspacio().equals(id)){
                return espacio;
            }
        }
        return null;
    }



    //***********************************************
    //
    // MÉTODOS DE GESTIÓN DE RESERVAS
    //
    //***********************************************

    public void crearReserva(Reserva reserva){
        validarReserva(reserva);

        if ((buscarReservaPorId(reserva.getIdReserva()) != null)){
            throw new IllegalArgumentException("Ya existe una reserva con ese ID");
        }

        comprobarDisponibilidad(reserva);

        reservas.add(reserva);
    }

    public ArrayList<Reserva> consultarReservas(){
        return new ArrayList<>(reservas);
    }

    public Reserva consultarReserva(String id){
        Reserva reserva = buscarReservaPorId(id);

        return reserva;
    }

    public ArrayList<Reserva> consultarPorProfesor(Profesor id){
        ArrayList<Reserva> resultado = new ArrayList<>();

        for (Reserva reserva : reservas){
            if (reserva.getProfesor() != null &&
                    reserva.getProfesor().equals(id)){
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public ArrayList<Reserva> consultarPorEspacio(Espacio id){
        ArrayList<Reserva> resultado = new ArrayList<>();

        for (Reserva reserva : reservas){
            if (reserva.getEspacio() != null &&
                    reserva.getEspacio().equals(id)){
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public void cancelarReserva(String id) {
        Reserva reserva = buscarReservaPorId(id);

        if (reserva.getEstado() == EstadoReserva.Cancelado) {
            throw new IllegalArgumentException("La reserva ya está cancelada");
        }

        reserva.setEstado(EstadoReserva.Cancelado);
    }

    private void comprobarDisponibilidad(Reserva nuevo) {
        Espacio espacio = buscarEspacioPorId(nuevo.getEspacio().getIdEspacio());
        Profesor profesor = buscarProfesorPorId(nuevo.getProfesor().getIdProfesor());

        if (!espacio.getDisponible().equals("DISPONIBLE")) {
            throw new IllegalArgumentException("El espacio no está disponible");
        }

        if (nuevo.getNumAsistentes() > espacio.getCapacidad()) {
            throw new IllegalArgumentException(
                    "Se supera la capacidad del espacio");
        }

        for (Reserva existente : reservas) {

            // Las reservas canceladas no bloquean horarios
            if (existente.getEstado() ==
                    EstadoReserva.Cancelado) {
                continue;
            }

            // Comparan reservas del mismo día
            if (!existente.getFecha().equals(nuevo.getFecha())) {
                continue;
            }

            boolean mismoEspacio =
                    existente.getEspacio() != null
                            && existente.getEspacio().getIdEspacio()
                            .equals(espacio.getIdEspacio());

            boolean mismoProfesor =
                    existente.getProfesor() != null
                            && existente.getProfesor().getIdProfesor()
                            .equals(profesor.getIdProfesor());

            boolean seSolapan =
                    nuevo.getHoraInicio().isBefore(
                            existente.getHoraFin())
                            && existente.getHoraInicio().isBefore(
                            nuevo.getHoraFin());

            if (seSolapan && mismoEspacio) {
                throw new IllegalArgumentException("El espacio ya tiene una reserva " + "en ese horario");
            }

            if (seSolapan && mismoProfesor) {
                throw new IllegalArgumentException("El profesor ya tiene una reserva " + "en ese horario");
            }
        }
    }

    private void validarReserva(Reserva reserva) {
        if (reserva == null){ throw new IllegalArgumentException("La reserva puede ser nula"); }
        if (reserva.getIdReserva() == null || reserva.getIdReserva().trim().isEmpty()){ throw new IllegalArgumentException("El ID es obligatorio"); }
        if (reserva.getFecha() == null){ throw new IllegalArgumentException("La fecha es obligatoria"); }
        if (reserva.getHoraInicio() == null || reserva.getHoraFin() == null){ throw new IllegalArgumentException("Las horas son obligatorias"); }
        if (reserva.getHoraInicio().isAfter(reserva.getHoraFin())){ throw new IllegalArgumentException("La hora final debe ser posterior a la inicial"); }
        if (reserva.getProfesor() == null){ throw new IllegalArgumentException("Debe indicarse un profesor"); }
        if (reserva.getEspacio() == null){ throw new IllegalArgumentException("Debe indicarse un espacio"); }
        if (reserva.getNumAsistentes() <= 0){ throw new IllegalArgumentException("Debe haber al menos un asistente"); }
        if (reserva.getEstado() == null){ throw new IllegalArgumentException("Debe indicarse el estado de la reserva"); }
    }

    private Reserva buscarReservaPorId(String id){
        for (Reserva reserva : reservas){
            if (reserva.getIdReserva().equals(id)){
                return reserva;
            }
        }
        return null;
    }
}
