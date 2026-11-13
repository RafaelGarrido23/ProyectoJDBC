package dao.fjson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.EspacioDAO;
import modelo.Departamento;
import modelo.Espacio;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class EspacioDAOFJS implements EspacioDAO {

    private final String ruta;

    public EspacioDAOFJS(String ruta) { this.ruta = ruta; }

    @Override
    public ArrayList<Espacio> leerFichero(){

        Gson gson = new Gson();

        try (FileReader flujoDatos =
                     new FileReader(ruta)){

            Type tipoLista =
                    new TypeToken<ArrayList<Espacio>>(){}.getType();
            ArrayList<Espacio> espacios =
                    gson.fromJson(flujoDatos, tipoLista);

            return espacios;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer los espacios", e);
        }
    }

    @Override
    public void escribirFichero(ArrayList<Espacio> espacios) {

        Gson gson = new Gson();

        try(FileWriter flujoDatos = new FileWriter(ruta);) {
            gson.toJson(espacios, flujoDatos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir las reservas", e);
        }
    }
}