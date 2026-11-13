package dao.fjson.adaptadoresJSON;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import modelo.Reserva;

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
                src.getProfesor() != null
                        ? src.getProfesor().getIdProfesor() : null);

        json.addProperty("idEspacio",
                src.getEspacio() != null
                        ? src.getEspacio().getIdEspacio() : null);

        json.addProperty("numAsistentes", src.getNumAsistentes());

        return json;
    }
}