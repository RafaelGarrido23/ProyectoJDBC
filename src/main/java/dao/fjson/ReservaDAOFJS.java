package dao.fjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dao.ReservaDAO;
import dao.fjson.adaptadoresJSON.*;
import modelo.Reserva;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReservaDAOFJS implements ReservaDAO {

    private final String ruta;

    public ReservaDAOFJS(String ruta) { this.ruta = ruta; }

    @Override
    public ArrayList<Reserva> leerFichero(){

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalTime.class, new AdaptadorLocalTime())
            .registerTypeAdapter(
                    LocalDate.class, new AdaptadorLocalDate())
            .registerTypeAdapter(
                    Reserva.class, new DeserializadorReserva())
            .create();

        try (FileReader flujoDatos = new FileReader(ruta)){

            Type tipoLista =
                new TypeToken<ArrayList<Reserva>>(){}.getType();

            ArrayList<Reserva> reservas =
                gson.fromJson(flujoDatos, tipoLista);

            return reservas;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer las reservas", e);
        }
    }

    @Override
    public void escribirFichero(ArrayList<Reserva> reservas) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalTime.class, new AdaptadorLocalTime())
                .registerTypeAdapter(
                        LocalDate.class, new AdaptadorLocalDate())
                .registerTypeAdapter(
                        Reserva.class, new SerializadorReserva())
                .setPrettyPrinting()
                .create();

        try (FileWriter flujoDatos = new FileWriter(ruta)) {
            gson.toJson(reservas, flujoDatos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir las reservas", e);
        }
    }
}
