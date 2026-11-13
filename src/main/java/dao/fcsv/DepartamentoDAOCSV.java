package dao.fcsv;

import dao.DepartamentoDAO;
import modelo.Departamento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DepartamentoDAOCSV implements DepartamentoDAO {

    private final String ruta;

    public DepartamentoDAOCSV(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Departamento> leerFichero() {
        ArrayList<Departamento> departamentos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                if (datos.length >= 2) {
                    Departamento dep = new Departamento(datos[0].trim(), datos[1].trim());
                    departamentos.add(dep);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero CSV de departamentos: " + e.getMessage());
        }
        return departamentos;
    }

    @Override
    public void escribirFichero(ArrayList<Departamento> departamentos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Departamento dep : departamentos) {
                bw.write(dep.getIdDepartamento() + ";" + dep.getNombre());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero CSV de departamentos: " + e.getMessage());
        }
    }
}
