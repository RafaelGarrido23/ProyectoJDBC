package dao.fjdbc;

import modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservaDAOJDBC {

    public void insertarReserva(Reserva reserva, Connection cn) throws SQLException {
        String sql = "INSERT INTO reservas\n" +
                "(id_reserva, fecha, hora_inicio, hora_fin, id_profesor, id_espacio, numero_asistentes, estado, motivo)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, reserva.getIdReserva());
        pst.setDate(2, Date.valueOf(reserva.getFecha()));
        pst.setTime(3, Time.valueOf(reserva.getHoraInicio()));
        pst.setTime(4, Time.valueOf(reserva.getHoraFin()));
        pst.setString(5, reserva.getProfesor().getIdProfesor());
        pst.setString(6, reserva.getEspacio().getIdEspacio());
        pst.setInt(7, reserva.getNumAsistentes());
        pst.setString(8, String.valueOf(reserva.getEstado()));

        pst.execute();
    }

    public ArrayList<Reserva> obtenerReservas(Connection cn) throws SQLException {
        String sql = "SELECT id_reserva, fecha, hora_inicio, hora_fin, id_profesor, id_espacio, numero_asistentes, estado, motivo\n" +
                "FROM reservas";

        PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<Reserva> reservas = new ArrayList<>();

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
                    estadoReserva
            );
            reservas.add(r);
        }

        return reservas;
    }

    public void modificarReserva(Reserva reserva, Connection cn) throws SQLException {
        String sql = "UPDATE reservas SET fecha = ?, hora_inicio = ?, hora_fin = ?, id_profesor = ?, id_espacio = ?, numero_asistentes = ?, estado = ?, motivo = ?\n" +
                "WHERE id_profesor = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setDate(2, Date.valueOf(reserva.getFecha()));
        pst.setTime(3, Time.valueOf(reserva.getHoraInicio()));
        pst.setTime(4, Time.valueOf(reserva.getHoraFin()));
        pst.setString(5, reserva.getProfesor().getIdProfesor());
        pst.setString(6, reserva.getEspacio().getIdEspacio());
        pst.setInt(7, reserva.getNumAsistentes());
        pst.setString(8, String.valueOf(reserva.getEstado()));

        pst.execute();
    }

    public void eliminarReserva(Reserva reserva, Connection cn) throws SQLException {
        String sql = "DELETE FROM reservas\n" +
                "WHERE id_reserva = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, reserva.getIdReserva());

        pst.execute();
    }
}
