package dao;

import modelo.Departamento;

import java.util.ArrayList;

public interface DepartamentoDAO {

    ArrayList<Departamento> leerFichero();

    void escribirFichero(ArrayList<Departamento> departamentos);
}
