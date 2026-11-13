package dao.fdat;

import dao.DepartamentoDAO;
import modelo.Departamento;

import java.io.*;
import java.util.ArrayList;

public class DepartamentoDAOFB implements DepartamentoDAO {

    private final String ruta;

    public DepartamentoDAOFB(String ruta) {
        this.ruta = ruta;
    }


    @Override
    public ArrayList<Departamento> leerFichero(){
        File archivo = new File(ruta);

        if (!archivo.exists() || archivo.length() == 0){
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))){
            return (ArrayList<Departamento>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer los departamentos", e);
        }

    }

    @Override
    public void escribirFichero (ArrayList<Departamento> departamentos){
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))){

            oos.writeObject(departamentos);

        } catch (IOException e){
            throw new RuntimeException("Error al escribir los departamentos", e);
        }
    }

}
