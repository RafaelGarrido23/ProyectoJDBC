package dao;

import modelo.Reserva;

import java.util.ArrayList;

public interface ReservaDAO {

    ArrayList<Reserva> leerFichero();

    void escribirFichero(ArrayList<Reserva> reservas);
}
