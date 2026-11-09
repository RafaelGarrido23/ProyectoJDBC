package org.example.dao;

import org.example.modelo.Departamento;

import java.util.ArrayList;

public interface DepartamentoDAO {

    ArrayList<Departamento> leerFichero();

    void escribirFichero(ArrayList<Departamento> departamentos);
}
