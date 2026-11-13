package dao.fcsv;

import dao.EspacioDAO;
import modelo.Espacio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EspacioDAOCSV implements EspacioDAO {

    private final String ruta;

    public EspacioDAOCSV(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Espacio> leerFichero() {
        ArrayList<Espacio> espacios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                if (datos.length >= 7) {
                    String id = datos[0].trim();
                    String nombre = datos[1].trim();
                    String tipo = datos[2].trim();
                    String ubicacion = datos[3].trim();
                    int capacidad = Integer.parseInt(datos[4].trim());
                    String equipamiento = datos[5].trim();
                    String estado = datos[6].trim();

                    Espacio espacio = new Espacio(id, nombre, tipo, ubicacion, capacidad, equipamiento, estado);
                    espacios.add(espacio);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero CSV de espacios: " + e.getMessage());
        }
        return espacios;
    }

    @Override
    public void escribirFichero(ArrayList<Espacio> espacios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Espacio esp : espacios) {
                bw.write(esp.getIdEspacio() + ";" +
                        esp.getNombre() + ";" +
                        esp.getTipo() + ";" +
                        esp.getUbicacion() + ";" +
                        esp.getCapacidad() + ";" +
                        esp.getEquipamiento() + ";" +
                        esp.getDisponible());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero CSV de espacios: " + e.getMessage());
        }
    }
}