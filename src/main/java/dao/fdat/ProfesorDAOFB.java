package dao.fdat;

import dao.ProfesorDAO;
import modelo.Profesor;

import java.io.*;
import java.util.ArrayList;

public class ProfesorDAOFB implements ProfesorDAO {

    private final String ruta;

    public ProfesorDAOFB(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Profesor> leerFichero(){
        File archivo = new File(ruta);

        if (!archivo.exists() || archivo.length() == 0){
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))){
            return (ArrayList<Profesor>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer los profesores", e);
        }

    }

    @Override
    public void escribirFichero (ArrayList<Profesor> profesores){
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))){

            oos.writeObject(profesores);

        } catch (IOException e){
            throw new RuntimeException("Error al escribir los profesores", e);
        }
    }
}
