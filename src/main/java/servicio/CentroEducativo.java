package servicio;

import dao.*;
import modelo.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

    private ConexionBD conexionBD;
    private Connection cn;

    public CentroEducativo(DepartamentoDAO departamentoDAO, ProfesorDAO profesorDAO, EspacioDAO espacioDAO, ReservaDAO reservaDAO) throws IOException, SQLException {
        this.departamentoDAO = departamentoDAO;
        this.profesorDAO = profesorDAO;
        this.espacioDAO = espacioDAO;
        this.reservaDAO = reservaDAO;

        conexionBD = new ConexionBD();
        cn = conexionBD.getConexion();
        leerTodo();
    }

    public void leerTodo() throws SQLException {
        departamentos = departamentoDAO.obtenerDepartamento(cn);
        profesores = profesorDAO.obtenerProfesores(cn);
        espacios = espacioDAO.obtenerEspacios(cn);
        reservas = reservaDAO.obtenerReservasConJoin(cn);
        asociarAProfesores();
        asociarAReservas();
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

    public Connection getConexion() throws SQLException {
        return cn;
    }

    //***********************************************
    //
    // MÉTODOS DE GESTIÓN DE DEPARTAMENTOS
    //
    //***********************************************

    public void altaDepartamento(Departamento departamento) throws SQLException{
        validarDepartamento(departamento);

        if ((buscarDepartamentoPorId(departamento.getIdDepartamento()) != null)){
            throw new IllegalArgumentException("Ya existe un departamento con ese ID");
        }

        departamentoDAO.insertarDepartamento(departamento, cn); // --> Pedro

        departamentos.add(departamento);
    }

    public void modificarDepartamento(Departamento modificado) throws SQLException{
        validarDepartamento(modificado);

        Departamento departamento =
                buscarDepartamentoPorId(modificado.getIdDepartamento());

        if(departamento == null){
            throw new IllegalArgumentException("El departamento no existe");
        }

        departamentoDAO.modificarDepartamento(modificado, cn);

        departamento.setNombre(modificado.getNombre());
    }

    public void bajaDepartamento(String id) throws SQLException{

        Departamento departamento = buscarDepartamentoPorId(id);
        validarDepartamento(departamento);

        boolean tieneProfesores = profesores.stream()
                .anyMatch(p -> p.getDepartamento() != null
                        && p.getDepartamento().getIdDepartamento().equals(id));

        if (tieneProfesores) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un departamento con profesores asociados");
        }

        departamentoDAO.eliminarDepartamento(departamento, cn);

        departamentos.remove(departamento);

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

    public void altaProfesor(Profesor profesor) throws SQLException{
        validarProfesor(profesor);

        if ((buscarProfesorPorId(profesor.getIdProfesor()) != null)){
            throw new IllegalArgumentException("Ya existe un profesor con ese ID");
        }

        // Comprobar que el departamento existe:
        Departamento departamento = buscarDepartamentoPorId(profesor.getDepartamento().getIdDepartamento());

        if (departamento == null){
            throw new IllegalArgumentException("El departamento no existe");
        }

        profesor.setDepartamento(departamento);

        profesorDAO.insertarProfesor(profesor, cn);

        profesores.add(profesor);
    }

    public void modificarProfesor(Profesor modificado) throws SQLException{
        validarProfesor(modificado);

        Profesor profesor = buscarProfesorPorId(
                modificado.getIdProfesor());

        if (profesor == null) {
            throw new IllegalArgumentException("El profesor no existe");
        }

        Departamento departamento = buscarDepartamentoPorId(
                modificado.getDepartamento().getIdDepartamento());

        if (departamento == null) {
            throw new IllegalArgumentException(
                    "El departamento no existe");
        }

        modificado.setDepartamento(departamento);

        profesorDAO.modificarProfesor(modificado, cn);

        profesor.setNombre(modificado.getNombre());
        profesor.setApellidos(modificado.getApellidos());
        profesor.setEmail(modificado.getEmail());
        profesor.setDepartamento(departamento);
    }

    public void bajaProfesor(String id) throws SQLException{
        Profesor profesor = buscarProfesorPorId(id);
        validarProfesor(profesor);

        boolean tieneReservas = reservas.stream()
                .anyMatch(r -> r.getProfesor() != null
                        && r.getProfesor().getIdProfesor().equals(id));

        if (tieneReservas) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un profesor que tiene reservas asociadas");
        }

        profesorDAO.eliminarProfesor(profesor, cn);

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


    public void altaEspacio(Espacio espacio) throws SQLException{
        validarEspacio(espacio);

        if ((buscarEspacioPorId(espacio.getIdEspacio()) != null)){
            throw new IllegalArgumentException("Ya existe un espacio con ese ID");
        }

        espacioDAO.insertarEspacio(espacio, cn);

        espacios.add(espacio);
    }

    public void modificarEspacio(Espacio modificado) throws SQLException{
        validarEspacio(modificado);

        Espacio espacio = buscarEspacioPorId(
                modificado.getIdEspacio());

        if (espacio == null) {
            throw new IllegalArgumentException("El espacio no existe");
        }

        espacioDAO.modificarEspacio(modificado, cn);

        espacio.setNombre(modificado.getNombre());
        espacio.setTipo(modificado.getTipo());
        espacio.setUbicacion(modificado.getUbicacion());
        espacio.setCapacidad(modificado.getCapacidad());
        espacio.setEquipamiento(modificado.getEquipamiento());
        espacio.setDisponible(modificado.isDisponible());

    }

    public void bajaEspacio(String id) throws SQLException{
        Espacio espacio = buscarEspacioPorId(id);
        validarEspacio(espacio);

        boolean tieneReservas = reservas.stream()
                .anyMatch(r -> r.getEspacio() != null
                        && r.getEspacio().getIdEspacio().equals(id));

        if (tieneReservas) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un espacio con reservas asociadas");
        }

        espacioDAO.eliminarEspacio(espacio, cn);

        espacios.remove(espacio);

    }

    public ArrayList<Espacio> consultarEspacios(){
        return new ArrayList<>(espacios);
    }

    public Espacio consultarEspacio(String id){
        Espacio espacio = buscarEspacioPorId(id);

        return espacio;
    }


    public void cambiarDisponibilidad(String id) throws SQLException {

        Espacio espacio = buscarEspacioPorId(id);

        if (espacio == null) {
            throw new IllegalArgumentException(
                    "El espacio no existe");
        }

        boolean nuevaDisponibilidad = !espacio.isDisponible();

        espacioDAO.cambiarDisponibilidad(
                id, nuevaDisponibilidad, cn);

        espacio.setDisponible(nuevaDisponibilidad);
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

    public void crearReserva(Reserva reserva) throws SQLException{
        validarReserva(reserva);

        if (buscarReservaPorId(reserva.getIdReserva()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una reserva con ese ID");
        }

        Profesor profesor = buscarProfesorPorId(
                reserva.getProfesor().getIdProfesor());

        Espacio espacio = buscarEspacioPorId(
                reserva.getEspacio().getIdEspacio());

        if (profesor == null) {
            throw new IllegalArgumentException("El profesor no existe");
        }

        if (espacio == null) {
            throw new IllegalArgumentException("El espacio no existe");
        }

        reserva.setProfesor(profesor);
        reserva.setEspacio(espacio);

        comprobarDisponibilidad(reserva);

        reservaDAO.insertarReserva(reserva, cn);

        reservas.add(reserva);

    }

    public void modificarReserva(Reserva modificado) throws SQLException{
        validarReserva(modificado);

        Reserva reserva = buscarReservaPorId(
                modificado.getIdReserva());

        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no existe");
        }

        Profesor profesor = buscarProfesorPorId(
                modificado.getProfesor().getIdProfesor());

        if (profesor == null) {
            throw new IllegalArgumentException(
                    "El profesor no existe");
        }

        Espacio espacio = buscarEspacioPorId(
                modificado.getEspacio().getIdEspacio());

        if (espacio == null) {
            throw new IllegalArgumentException(
                    "La reserva no existe");
        }

        modificado.setProfesor(profesor);
        modificado.setEspacio(espacio);

        reservaDAO.modificarReserva(modificado, cn);

        reserva.setFecha(modificado.getFecha());
        reserva.setHoraInicio(modificado.getHoraInicio());
        reserva.setHoraFin(modificado.getHoraFin());
        reserva.setProfesor(profesor);
        reserva.setEspacio(espacio);
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

    public void confirmarReserva(String id) throws SQLException {

        Reserva reserva = buscarReservaPorId(id);

        if (reserva == null) {
            throw new IllegalArgumentException(
                    "La reserva no existe");
        }

        if (reserva.getEstado() == EstadoReserva.Confirmado) {
            throw new IllegalArgumentException(
                    "La reserva ya está confirmada");
        }

        reservaDAO.cancelarReserva(id, cn);

        reserva.setEstado(EstadoReserva.Confirmado);
    }

    public void finalizarReserva(String id) throws SQLException {

        Reserva reserva = buscarReservaPorId(id);

        if (reserva == null) {
            throw new IllegalArgumentException(
                    "La reserva no existe");
        }

        if (reserva.getEstado() == EstadoReserva.Finalizado) {
            throw new IllegalArgumentException(
                    "La reserva ya está finalizada");
        }

        reservaDAO.finalizarReserva(id, cn);

        reserva.setEstado(EstadoReserva.Finalizado);
    }

    public void cancelarReserva(String id) throws SQLException {

        Reserva reserva = buscarReservaPorId(id);

        if (reserva == null) {
            throw new IllegalArgumentException(
                    "La reserva no existe");
        }

        if (reserva.getEstado() == EstadoReserva.Cancelado) {
            throw new IllegalArgumentException(
                    "La reserva ya está cancelada");
        }

        reservaDAO.cancelarReserva(id, cn);

        reserva.setEstado(EstadoReserva.Cancelado);
    }

    public void eliminarReserva(String id) throws SQLException{
        Reserva reserva = buscarReservaPorId(id);
        validarReserva(reserva);

        reservaDAO.eliminarReserva(reserva, cn);

        reservas.remove(reserva);
    }

    private void comprobarDisponibilidad(Reserva nuevo) {
        Espacio espacio = buscarEspacioPorId(nuevo.getEspacio().getIdEspacio());
        Profesor profesor = buscarProfesorPorId(nuevo.getProfesor().getIdProfesor());

        if (!espacio.isDisponible()) {
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
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())){ throw new IllegalArgumentException("La hora final debe ser posterior a la inicial"); }
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


    public ArrayList<Reserva> consultarPorProfesor(String id)
            throws SQLException {

        if (buscarProfesorPorId(id) == null) {
            throw new IllegalArgumentException(
                    "El profesor no existe");
        }

        ArrayList<Reserva> resultado =
                reservaDAO.obtenerReservasPorProfesor(id, cn);

        for (Reserva r : resultado) {
            r.setProfesor(buscarProfesorPorId(
                    r.getProfesor().getIdProfesor()));

            r.setEspacio(buscarEspacioPorId(
                    r.getEspacio().getIdEspacio()));
        }

        return resultado;
    }

    public ArrayList<Reserva> consultarPorEspacio(String id)
            throws SQLException {

        if (buscarEspacioPorId(id) == null) {
            throw new IllegalArgumentException("El espacio no existe");
        }

        ArrayList<Reserva> resultado =
                reservaDAO.obtenerReservasPorEspacio(id, cn);

        for (Reserva r : resultado) {
            r.setProfesor(buscarProfesorPorId(
                    r.getProfesor().getIdProfesor()));

            r.setEspacio(buscarEspacioPorId(
                    r.getEspacio().getIdEspacio()));
        }

        return resultado;
    }

    public int contarReservasProfesor(String idProfesor) throws SQLException {

        if (buscarProfesorPorId(idProfesor) == null) {
            throw new IllegalArgumentException("El profesor no existe");
        }
        return reservaDAO.contarReservasProfesor(idProfesor, cn);
    }
}
