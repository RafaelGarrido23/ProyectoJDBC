package dao;

import modelo.Espacio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EspacioDAO {

    public void insertarEspacio(Espacio espacio, Connection cn)
            throws SQLException {
        String sql = "INSERT INTO espacios\n" +
                "(id_espacio, nombre, tipo, ubicacion, capacidad, equipamiento, disponible)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, espacio.getIdEspacio());
            pst.setString(2, espacio.getNombre());
            pst.setString(3, espacio.getTipo());
            pst.setString(4, espacio.getUbicacion());
            pst.setInt(5, espacio.getCapacidad());
            pst.setString(6, espacio.getEquipamiento());
            pst.setBoolean(7, espacio.isDisponible());
            pst.execute();
        }
    }

    public ArrayList<Espacio> obtenerEspacios(Connection cn)
            throws SQLException {
        String sql = "SELECT *\n" +
                "FROM espacios\n" +
                "ORDER BY id_espacio";

        ArrayList<Espacio> espacios = new ArrayList<>();

        try(PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()) {


            while (rs.next()) {
                Espacio e = new Espacio(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getBoolean(7)
                );
                espacios.add(e);
            }
        }

        return espacios;
    }

    public void modificarEspacio(Espacio espacio, Connection cn)
            throws SQLException {
        String sql = "UPDATE espacios SET nombre = ?, tipo = ?, ubicacion = ?, " +
                "capacidad = ?, equipamiento = ?, disponible = ?\n" +
                "WHERE id_espacio = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, espacio.getNombre());
            pst.setString(2, espacio.getTipo());
            pst.setString(3, espacio.getUbicacion());
            pst.setInt(4, espacio.getCapacidad());
            pst.setString(5, espacio.getEquipamiento());
            pst.setBoolean(6, espacio.isDisponible());
            pst.setString(7, espacio.getIdEspacio());
            pst.execute();
        }
    }

    public void cambiarDisponibilidad(String id, boolean disponible, Connection cn)
            throws SQLException {

        String sql = "UPDATE espacios SET disponible = ? " +
                "WHERE id_espacio = ?";

        try (PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setBoolean(1, disponible);
            pst.setString(2, id);
            pst.executeUpdate();
        }
    }


    public void eliminarEspacio(Espacio espacio, Connection cn)
                throws SQLException {
            String sql = "DELETE FROM espacios\n" +
                    "WHERE id_espacio = ?";

            try(PreparedStatement pst = cn.prepareStatement(sql)) {
                pst.setString(1, espacio.getIdEspacio());
                pst.execute();
            }
    }


}
