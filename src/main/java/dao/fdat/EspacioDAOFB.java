package dao.fdat;

import dao.EspacioDAO;
import modelo.Espacio;

import java.io.*;
import java.util.ArrayList;

public class EspacioDAOFB implements EspacioDAO {

    private final String ruta;

    public EspacioDAOFB(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Espacio> leerFichero(){
        File archivo = new File(ruta);

        if (!archivo.exists() || archivo.length() == 0){
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))){
            return (ArrayList<Espacio>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer los espacios", e);
        }

    }

    @Override
    public void escribirFichero (ArrayList<Espacio> espacios){
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))){

            oos.writeObject(espacios);

        } catch (IOException e){
            throw new RuntimeException("Error al escribir los espacios", e);
        }
    }
}
