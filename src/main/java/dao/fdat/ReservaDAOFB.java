package dao.fdat;

import dao.ReservaDAO;
import modelo.Reserva;

import java.io.*;
import java.util.ArrayList;

public class ReservaDAOFB implements ReservaDAO {

    private final String ruta;

    public ReservaDAOFB(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public ArrayList<Reserva> leerFichero(){
        File archivo = new File(ruta);

        if (!archivo.exists() || archivo.length() == 0){
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))){
            return (ArrayList<Reserva>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer las reservas", e);
        }

    }

    @Override
    public void escribirFichero (ArrayList<Reserva> reservas){
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))){

            oos.writeObject(reservas);

        } catch (IOException e){
            throw new RuntimeException("Error al escribir las reservas", e);
        }
    }
}
