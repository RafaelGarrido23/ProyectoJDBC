package dao;

import modelo.Departamento;
import modelo.Profesor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProfesorDAO {

    public void insertarProfesor(Profesor profesor, Connection cn)
            throws SQLException {
        String sql = "INSERT INTO profesores\n" +
                "(id_profesor, nombre, apellidos, email, id_departamento)\n" +
                "VALUES(?, ?, ?, ?, ?);";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, profesor.getIdProfesor());
            pst.setString(2, profesor.getNombre());
            pst.setString(3, profesor.getApellidos());
            pst.setString(4, profesor.getEmail());
            pst.setString(5, profesor.getDepartamento().getIdDepartamento());
            pst.execute();
        }
    }

    public ArrayList<Profesor> obtenerProfesores(Connection cn)
            throws SQLException {
        String sql = "SELECT *\n" +
                "FROM profesores\n" +
                "ORDER BY id_profesor";

        ArrayList<Profesor> profesores = new ArrayList<>();

        try(PreparedStatement pst = cn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Departamento departamento =
                        new Departamento(rs.getString(5));

                Profesor p = new Profesor(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        departamento
                );
                profesores.add(p);
            }
        }

        return profesores;
    }

    public void modificarProfesor(Profesor profesor, Connection cn)
            throws SQLException {
        String sql = "UPDATE profesores SET nombre = ?, apellidos = ?, email = ?,id_departamento = ?\n" +
                "WHERE id_profesor = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, profesor.getNombre());
            pst.setString(2, profesor.getApellidos());
            pst.setString(3, profesor.getEmail());
            pst.setString(4, profesor.getDepartamento().getIdDepartamento());
            pst.setString(5, profesor.getIdProfesor());
            pst.execute();
        }
    }

    public ArrayList<Profesor> obtenerProfesoresPorDepartamento
            (String idDepartamento, Connection cn) throws SQLException {

        String sql = "SELECT * FROM profesores " +
                "WHERE id_departamento = ? ORDER BY id_profesor";

        ArrayList<Profesor> profesores = new ArrayList<>();

        try (PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, idDepartamento);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Departamento departamento =
                            new Departamento(rs.getString(5));

                    Profesor profesor = new Profesor(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            departamento
                    );

                    profesores.add(profesor);
                }
            }
        }

        return profesores;
    }

    public void eliminarProfesor(Profesor profesor, Connection cn) throws SQLException {
        String sql = "DELETE FROM profesores\n" +
                "WHERE id_profesor = ?";

        try(PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, profesor.getIdProfesor());
            pst.execute();
        }
    }
}
