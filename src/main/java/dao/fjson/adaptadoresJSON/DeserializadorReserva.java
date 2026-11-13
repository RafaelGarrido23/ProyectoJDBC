package dao.fjson.adaptadoresJSON;

import com.google.gson.*;
import modelo.Espacio;
import modelo.EstadoReserva;
import modelo.Profesor;
import modelo.Reserva;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class DeserializadorReserva
        implements JsonDeserializer<Reserva> {

    @Override
    public Reserva deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        Reserva reserva = new Reserva();

        reserva.setIdReserva(jsonObject.get("idReserva").getAsString());
        reserva.setFecha(context.deserialize(jsonObject.get("fecha"), LocalDate.class));
        reserva.setHoraInicio(context.deserialize(jsonObject.get("horaInicio"), LocalTime.class));
        reserva.setHoraFin(context.deserialize(jsonObject.get("horaFin"), LocalTime.class));
        reserva.setNumAsistentes(jsonObject.get("numAsistentes").getAsInt());
        reserva.setEstado(EstadoReserva.valueOf(jsonObject.get("estado").getAsString()));

        if (jsonObject.has("idProfesor")) {
            String idProfesorStr = jsonObject.get("idProfesor").getAsString();
            reserva.setProfesor(new Profesor(idProfesorStr));
        }

        if (jsonObject.has("idProfesor")) {
            String idEspacioStr = jsonObject.get("idEspacio").getAsString();
            reserva.setEspacio(new Espacio(idEspacioStr));
        }

        return reserva;
    }
}
