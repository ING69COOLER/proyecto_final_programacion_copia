package co.edu.uniquindio.poo.Balance.DateBalance;

import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Persona;

public interface ICalculoBalance {
    //retorna el balance de las sillas segun x evento, recibe id del personas :-)
   double obtenerBalance(ArrayList<Persona> listaPersonas ) ; 
}
