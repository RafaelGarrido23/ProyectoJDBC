package org.example.dao;

import org.example.modelo.Profesor;

import java.util.ArrayList;

public interface ProfesorDAO {

    ArrayList<Profesor> leerFichero();

    void escribirFichero(ArrayList<Profesor> profesores);
}
