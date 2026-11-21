package dao;

import modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class ReservaDAO {

    public void insertarReserva(Reserva reserva, Connection cn) throws SQLException {
        String sql = "INSERT INTO reservas\n" +
                "(id_reserva, fecha, hora_inicio, hora_fin, id_profesor, id_espacio, numero_asistentes, estado, motivo)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, reserva.getIdReserva());
            pst.setDate(2, Date.valueOf(reserva.getFecha()));
            pst.setTime(3, Time.valueOf(reserva.getHoraInicio()));
            pst.setTime(4, Time.valueOf(reserva.getHoraFin()));
            pst.setString(5, reserva.getProfesor().getIdProfesor());
            pst.setString(6, reserva.getEspacio().getIdEspacio());
            pst.setInt(7, reserva.getNumAsistentes());
            // pst.setString(8, String.valueOf(reserva.getEstado())); (otra alternativa)
            pst.setString(8, reserva.getEstado().name());
            pst.setString(9, reserva.getMotivo());
            pst.execute();
        }
    }

    public ArrayList<Reserva> obtenerReservas(Connection cn) throws SQLException {
        String sql = "SELECT *\n" +
                "FROM reservas\n" +
                "ORDER BY id_reserva";

        ArrayList<Reserva> reservas = new ArrayList<>();

        try (PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()){

            while (rs.next()) {
                Profesor profesor = new Profesor(rs.getString(5));
                Espacio espacio = new Espacio(rs.getString(6));
                EstadoReserva estadoReserva = EstadoReserva.valueOf(rs.getString(8));

                Reserva r = new Reserva(
                        rs.getString(1),
                        rs.getDate(2).toLocalDate(),
                        rs.getTime(3).toLocalTime(),
                        rs.getTime(4).toLocalTime(),
                        profesor,
                        espacio,
                        rs.getInt(7),
                        estadoReserva,
                        rs.getString(9)
                );
                reservas.add(r);
            }
        }
        return reservas;
    }

    public void modificarReserva(Reserva reserva, Connection cn) throws SQLException {
        String sql = "UPDATE reservas SET fecha = ?, hora_inicio = ?, hora_fin = ?, id_profesor = ?, id_espacio = ?, numero_asistentes = ?, estado = ?, motivo = ?\n" +
                "WHERE id_reserva = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(reserva.getFecha()));
            pst.setTime(2, Time.valueOf(reserva.getHoraInicio()));
            pst.setTime(3, Time.valueOf(reserva.getHoraFin()));
            pst.setString(4, reserva.getProfesor().getIdProfesor());
            pst.setString(5, reserva.getEspacio().getIdEspacio());
            pst.setInt(6, reserva.getNumAsistentes());
            pst.setString(7, reserva.getEstado().name());
            pst.setString(8, reserva.getMotivo());
            pst.setString(9, reserva.getIdReserva());
            pst.execute();
        }
    }

    public void cancelarReserva(String id, Connection cn)
            throws SQLException {

        String sql = "UPDATE reservas SET estado = ? " +
                "WHERE id_reserva = ?";

        try (PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, EstadoReserva.Cancelado.name());
            pst.setString(2, id);
            pst.execute();
        }
    }


    public ArrayList<Reserva> obtenerReservasConJoin(Connection cn)
            throws SQLException {

        String sql = "SELECT r.*, " +
                "p.nombre AS nombre_profesor, " +
                "p.apellidos AS apellidos_profesor, " +
                "p.email AS email_profesor, " +
                "p.id_departamento, " +
                "e.nombre AS nombre_espacio, " +
                "e.tipo, e.ubicacion, e.capacidad, " +
                "e.equipamiento, e.disponible " +
                "FROM reservas r " +
                "INNER JOIN profesores p " +
                "ON r.id_profesor = p.id_profesor " +
                "INNER JOIN espacios e " +
                "ON r.id_espacio = e.id_espacio " +
                "ORDER BY r.fecha, r.hora_inicio";

        ArrayList<Reserva> reservas = new ArrayList<>();

        try (PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Profesor profesor = new Profesor(
                        rs.getString(5),
                        rs.getString(10),
                        rs.getString(11),
                        rs.getString(12),
                        new Departamento(rs.getString(13))
                );

                Espacio espacio = new Espacio(
                        rs.getString(6),
                        rs.getString(14),
                        rs.getString(15),
                        rs.getString(16),
                        rs.getInt(17),
                        rs.getString(18),
                        rs.getBoolean(19)
                );

                Reserva reserva = new Reserva(
                        rs.getString(1),
                        rs.getDate(2).toLocalDate(),
                        rs.getTime(3).toLocalTime(),
                        rs.getTime(4).toLocalTime(),
                        profesor,
                        espacio,
                        rs.getInt(7),
                        EstadoReserva.valueOf(rs.getString(8)),
                        rs.getString(9)
                );

                reservas.add(reserva);
            }
        }

        return reservas;
    }

    public int contarReservasProfesor(String idProfesor, Connection cn)
            throws SQLException {

        String sql = "{? = CALL fn_contar_reservas_profesor(?)}";

        try (java.sql.CallableStatement cs = cn.prepareCall(sql)) {

            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setString(2, idProfesor);
            cs.execute();
            return cs.getInt(1);
        }
    }


    public ArrayList<Reserva> obtenerReservasPorProfesor(
            String idProfesor, Connection cn) throws SQLException {

        String sql = "SELECT * FROM reservas " +
                "WHERE id_profesor = ? ORDER BY fecha, hora_inicio";

        ArrayList<Reserva> resultado = new ArrayList<>();

        try (PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, idProfesor);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Profesor profesor = new Profesor(rs.getString(5));
                    Espacio espacio = new Espacio(rs.getString(6));
                    Reserva r = new Reserva(
                            rs.getString(1),
                            rs.getDate(2).toLocalDate(),
                            rs.getTime(3).toLocalTime(),
                            rs.getTime(4).toLocalTime(),
                            profesor,
                            espacio,
                            rs.getInt(7),
                            EstadoReserva.valueOf(rs.getString(8)),
                            rs.getString(9)
                    );

                    resultado.add(r);
                }
            }
        }

        return resultado;
    }

    public ArrayList<Reserva> obtenerReservasPorEspacio(
            String idEspacio, Connection cn) throws SQLException {

        String sql = "SELECT * FROM reservas " +
                "WHERE id_espacio = ? ORDER BY fecha, hora_inicio";

        ArrayList<Reserva> resultado = new ArrayList<>();

        try (PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setString(1, idEspacio);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Profesor profesor = new Profesor(rs.getString(5));
                    Espacio espacio = new Espacio(rs.getString(6));

                    Reserva r = new Reserva(
                            rs.getString(1),
                            rs.getDate(2).toLocalDate(),
                            rs.getTime(3).toLocalTime(),
                            rs.getTime(4).toLocalTime(),
                            profesor,
                            espacio,
                            rs.getInt(7),
                            EstadoReserva.valueOf(rs.getString(8)),
                            rs.getString(9)
                    );

                    resultado.add(r);
                }
            }
        }

        return resultado;
    }

    public void eliminarReserva(Reserva reserva, Connection cn) throws SQLException {
        String sql = "DELETE FROM reservas\n" +
                "WHERE id_reserva = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, reserva.getIdReserva());
            pst.execute();
        }
    }
}
