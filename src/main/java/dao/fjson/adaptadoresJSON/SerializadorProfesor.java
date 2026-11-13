package dao.fjson.adaptadoresJSON;

import com.google.gson.*;
import modelo.Profesor;

import java.lang.reflect.Type;

public class SerializadorProfesor implements JsonSerializer<Profesor> {

    @Override
    public JsonElement serialize(
            Profesor src,
            Type typeOfSrc,
            JsonSerializationContext context) {

        JsonObject json = new JsonObject();

        json.addProperty("idProfesor", src.getIdProfesor());
        json.addProperty("nombre", src.getNombre());
        json.addProperty("apellidos", src.getApellidos());
        json.addProperty("email", src.getEmail());

        json.addProperty("idDepartamento",
                src.getDepartamento() != null
                        ? src.getDepartamento().getIdDepartamento() : null);

        return json;
    }
}
