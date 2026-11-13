package dao.fjdbc;

import modelo.Espacio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EspacioDAOJDBC {

    public void insertarEspacio(Espacio espacio, Connection cn) throws SQLException {
        String sql = "INSERT INTO espacios\n" +
                "(id_espacio, nombre, tipo, ubicacion, capacidad, equipamiento, disponible)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, espacio.getIdEspacio());
        pst.setString(2, espacio.getNombre());
        pst.setString(3, espacio.getTipo());
        pst.setString(4, espacio.getUbicacion());
        pst.setInt(5, espacio.getCapacidad());
        pst.setString(6, espacio.getEquipamiento());
        pst.setString(7, espacio.getDisponible());

        pst.execute();
    }

    public ArrayList<Espacio> obtenerEspacios(Connection cn) throws SQLException {
        String sql = "SELECT *\n" +
                "FROM espacios";

        PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<Espacio> espacios = new ArrayList<>();

        while (rs.next()) {
            Espacio e = new Espacio(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getString(7)
            );
            espacios.add(e);
        }

        return espacios;
    }

    public void modificarEspacio(Espacio espacio, Connection cn) throws SQLException {
        String sql = "UPDATE espacios SET nombre = ?, tipo = ?, ubicacion = ?, capacidad = ?, equipamiento = ?, estado = ?\n" +
                "WHERE id_espacio = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(2, espacio.getNombre());

        pst.execute();
    }

    public void eliminarEspacio(Espacio espacio, Connection cn) throws SQLException {
        String sql = "DELETE FROM espacios\n" +
                "WHERE id_espacio = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, espacio.getIdEspacio()
        );

        pst.execute();
    }
}
