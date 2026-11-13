package dao.fjdbc;

import modelo.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DepartamentoDAOJDBC {

    public void insertarDepartamento(Departamento departamento, Connection cn) throws SQLException {
        String sql = "INSERT INTO departamentos\n" +
                "(id_departamento, nombre)\n" +
                "VALUES(?, ?);";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, departamento.getIdDepartamento());
        pst.setString(2, departamento.getNombre());

        pst.execute();
    }

    public ArrayList<Departamento> obtenerDepartamento(Connection cn) throws SQLException {
        String sql = "SELECT id_departamento, nombre\n" +
                "FROM departamentos";

        PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<Departamento> departamentos = new ArrayList<>();

        while (rs.next()) {
            Departamento d = new Departamento(
                    rs.getString(1),
                    rs.getString(2)
            );
            departamentos.add(d);
        }

        return departamentos;
    }

    public void modificarDepartamento(Departamento departamento, Connection cn) throws SQLException {
        String sql = "UPDATE departamentos SET nombre = ?\n" +
                "WHERE id_departamento = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(2, departamento.getNombre());

        pst.execute();
    }

    public void eliminarDepartamento(Departamento departamento, Connection cn) throws SQLException {
        String sql = "DELETE FROM departamentos\n" +
                "WHERE id_departamento = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, departamento.getIdDepartamento());

        pst.execute();
    }
}
