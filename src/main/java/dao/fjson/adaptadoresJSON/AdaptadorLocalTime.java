package dao.fjson.adaptadoresJSON;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

public class AdaptadorLocalTime extends TypeAdapter<LocalTime> {

    @Override
    public LocalTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return LocalTime.parse(in.nextString());
    }

    @Override
    public void write(JsonWriter out, LocalTime hora) throws IOException {
        if (hora == null) {
            out.nullValue();
        } else {
            out.value(hora.toString());
        }
    }
}