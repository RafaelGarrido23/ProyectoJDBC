package org.example.dao.fjson.adaptadoresJSON;

import com.google.gson.*;
import org.example.modelo.Departamento;
import org.example.modelo.Profesor;

import java.lang.reflect.Type;

public class DeserializadorProfesor
        implements JsonDeserializer<Profesor> {

    @Override
    public Profesor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        Profesor profesor = new Profesor();

        profesor.setIdProfesor(jsonObject.get("idProfesor").getAsString());
        profesor.setNombre(jsonObject.get("nombre").getAsString());
        profesor.setApellidos(jsonObject.get("apellidos").getAsString());
        profesor.setEmail(jsonObject.get("email").getAsString());

        if (jsonObject.has("idDepartamento")) {
            String idDepartamentoStr = jsonObject.get("idDepartamento").getAsString();
            profesor.setIdDepartamento(new Departamento(idDepartamentoStr));
        }

        return profesor;
    }
}
