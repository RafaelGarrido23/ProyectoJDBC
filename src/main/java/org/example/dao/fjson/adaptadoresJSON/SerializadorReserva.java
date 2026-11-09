package org.example.dao.fjson.adaptadoresJSON;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.example.modelo.Reserva;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class SerializadorReserva implements JsonSerializer<Reserva> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public JsonElement serialize(
            Reserva src,
            Type typeOfSrc,
            JsonSerializationContext context) {

        JsonObject json = new JsonObject();

        json.addProperty("idReserva", src.getIdReserva());
        json.addProperty("fecha", src.getFecha().format(formatter));
        json.addProperty("horaInicio", src.getHoraInicio().toString());
        json.addProperty("horaFin", src.getHoraFin().toString());
        json.addProperty("estado", src.getEstado().name());

        json.addProperty("idProfesor",
                src.getIdProfesor() != null
                        ? src.getIdProfesor().getIdProfesor() : null);

        json.addProperty("idEspacio",
                src.getIdEspacio() != null
                        ? src.getIdEspacio().getIdEspacio() : null);

        json.addProperty("numAsistentes", src.getNumAsistentes());

        return json;
    }
}