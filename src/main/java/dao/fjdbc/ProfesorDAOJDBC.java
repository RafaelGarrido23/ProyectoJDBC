package dao.fjdbc;

import modelo.Departamento;
import modelo.Profesor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProfesorDAOJDBC {

    public void insertarProfesor(Profesor profesor, Connection cn) throws SQLException {
        String sql = "INSERT INTO profesores\n" +
                "(id_profesor, nombre, apellidos, email, id_departamento)\n" +
                "VALUES(?, ?, ?, ?, ?);";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, profesor.getIdProfesor());
        pst.setString(2, profesor.getNombre());
        pst.setString(3, profesor.getApellidos());
        pst.setString(4, profesor.getEmail());
        pst.setString(5, profesor.getDepartamento().getIdDepartamento());

        pst.execute();
    }

    public ArrayList<Profesor> obtenerProfesores(Connection cn) throws SQLException {
        String sql = "SELECT id_profesor, nombre, apellidos, email, id_departamento\n" +
                "FROM profesores";

        PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<Profesor> profesores = new ArrayList<>();

        while (rs.next()) {
            Departamento departamento = new Departamento(rs.getString(5));
            Profesor p = new Profesor(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    departamento

            );
            profesores.add(p);
        }

        return profesores;
    }

    public void modificarProfesor(Profesor profesor, Connection cn) throws SQLException {
        String sql = "UPDATE profesores SET nombre = ?, apellidos = ?, email = ?,id_departamento = ?\n" +
                "WHERE id_profesor = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(2, profesor.getNombre());
        pst.setString(3, profesor.getApellidos());
        pst.setString(4, profesor.getEmail());
        pst.setString(5, profesor.getDepartamento().getIdDepartamento());

        pst.execute();
    }

    public void eliminarProfesor(Profesor profesor, Connection cn) throws SQLException {
        String sql = "DELETE FROM profesores\n" +
                "WHERE id_profesor = ?";

        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, profesor.getIdProfesor());

        pst.execute();
    }
}
