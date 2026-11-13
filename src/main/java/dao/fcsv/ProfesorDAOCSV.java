package dao.fcsv;

import dao.ProfesorDAO;
import modelo.Departamento;
import modelo.Profesor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ProfesorDAOCSV implements ProfesorDAO {

    private final String ruta;

    public ProfesorDAOCSV(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Profesor> leerFichero() {
        ArrayList<Profesor> profesores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    String idProfesor = datos[0].trim();
                    String nombre = datos[1].trim();
                    String apellidos = datos[2].trim();
                    String email = datos[3].trim();
                    String idDepartamento = datos[4].trim();

                    Departamento dep = new Departamento(idDepartamento);
                    Profesor profesor = new Profesor(idProfesor, nombre, apellidos, email, dep);
                    profesores.add(profesor);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero CSV de profesores: " + e.getMessage());
        }
        return profesores;
    }

    @Override
    public void escribirFichero(ArrayList<Profesor> profesores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Profesor prof : profesores) {
                String idDep = prof.getDepartamento() != null ? prof.getDepartamento().getIdDepartamento() : "";
                bw.write(prof.getIdProfesor() + ";" +
                        prof.getNombre() + ";" +
                        prof.getApellidos() + ";" +
                        prof.getEmail() + ";" +
                        idDep);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero CSV de profesores: " + e.getMessage());
        }
    }
}