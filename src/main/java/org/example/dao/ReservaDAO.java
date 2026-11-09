package org.example.dao;

import org.example.modelo.Reserva;

import java.util.ArrayList;

public interface ReservaDAO {

    ArrayList<Reserva> leerFichero();

    void escribirFichero(ArrayList<Reserva> reservas);
}
