package org.example.dao;

import org.example.modelo.Espacio;

import java.util.ArrayList;

public interface EspacioDAO {

    ArrayList<Espacio> leerFichero();

    void escribirFichero(ArrayList<Espacio> espacios);

}
