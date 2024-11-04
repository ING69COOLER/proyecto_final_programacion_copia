package co.edu.uniquindio.poo.Balance.DateBalance;

import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Persona;

public class SillaVip implements ICalculoBalance {

    @Override
    public double obtenerBalance(ArrayList<Persona> listaPersonas) {
       double balance=0;
        for (Persona persona : listaPersonas) {
            balance += persona.getTotalPagar();
        }
        return balance;
    }
       

        
        
    
}
