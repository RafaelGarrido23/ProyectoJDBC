package dao;

import modelo.Espacio;

import java.util.ArrayList;

public interface EspacioDAO {

    ArrayList<Espacio> leerFichero();

    void escribirFichero(ArrayList<Espacio> espacios);

}
