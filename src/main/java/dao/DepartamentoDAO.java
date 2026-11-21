package dao;

import modelo.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DepartamentoDAO {

    public void insertarDepartamento(Departamento departamento, Connection cn)
            throws SQLException {
        String sql = "INSERT INTO departamentos\n" +
                "(id_departamento, nombre)\n" +
                "VALUES(?, ?);";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, departamento.getIdDepartamento());
            pst.setString(2, departamento.getNombre());
            pst.execute();
        }
    }

    public ArrayList<Departamento> obtenerDepartamento(Connection cn)
            throws SQLException {
        String sql = "SELECT *\n" +
                "FROM departamentos\n" +
                "ORDER BY id_departamento";

        ArrayList<Departamento> departamentos = new ArrayList<>();

        try(PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Departamento d = new Departamento(
                        rs.getString(1),
                        rs.getString(2)
                );
                departamentos.add(d);
            }

            return departamentos;
        }
    }

    public void modificarDepartamento(Departamento departamento, Connection cn)
            throws SQLException {
        String sql = "UPDATE departamentos SET nombre = ?\n" +
                "WHERE id_departamento = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, departamento.getNombre());
            pst.setString(2, departamento.getIdDepartamento());
            pst.execute();
        }
    }

    public void eliminarDepartamento(Departamento departamento, Connection cn)
            throws SQLException {
        String sql = "DELETE FROM departamentos\n" +
                "WHERE id_departamento = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, departamento.getIdDepartamento());
            pst.execute();
        }
    }
}
