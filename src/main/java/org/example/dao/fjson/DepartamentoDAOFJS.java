package org.example.dao.fjson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.dao.DepartamentoDAO;
import org.example.modelo.Departamento;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DepartamentoDAOFJS implements DepartamentoDAO {

    private final String ruta;

    public DepartamentoDAOFJS(String ruta) { this.ruta = ruta; }

    @Override
    public ArrayList<Departamento> leerFichero(){

        Gson gson = new Gson();

        try (FileReader flujoDatos =
                new FileReader(ruta)){

            Type tipoLista =
                new TypeToken<ArrayList<Departamento>>(){}.getType();
            ArrayList<Departamento> departamentos =
                gson.fromJson(flujoDatos, tipoLista);

            return departamentos;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer los departamentos", e);
        }
    }

    @Override
    public void escribirFichero(ArrayList<Departamento> departamentos) {

        Gson gson = new Gson();

        try(FileWriter flujoDatos = new FileWriter(ruta);) {
            gson.toJson(departamentos, flujoDatos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir las reservas", e);
        }
    }

}
