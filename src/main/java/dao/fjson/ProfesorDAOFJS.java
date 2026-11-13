package dao.fjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dao.ProfesorDAO;
import dao.fjson.adaptadoresJSON.DeserializadorProfesor;
import dao.fjson.adaptadoresJSON.SerializadorProfesor;
import modelo.Profesor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProfesorDAOFJS implements ProfesorDAO {

    private final String ruta;

    public ProfesorDAOFJS(String ruta) { this.ruta = ruta; }

    @Override
    public ArrayList<Profesor> leerFichero(){

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    Profesor.class, new DeserializadorProfesor())
            .create();

        try (FileReader flujoDatos =
                     new FileReader(ruta)){

            Type tipoLista =
                    new TypeToken<ArrayList<Profesor>>(){}.getType();
            ArrayList<Profesor> profesores =
                    gson.fromJson(flujoDatos, tipoLista);

            return profesores;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer los profesores", e);
        }
    }

    @Override
    public void escribirFichero(ArrayList<Profesor> profesores) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        Profesor.class, new SerializadorProfesor())
                .setPrettyPrinting()
                .create();

        try (FileWriter flujoDatos = new FileWriter(ruta)) {
            gson.toJson(profesores, flujoDatos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir los profesores", e);
        }
    }


}
