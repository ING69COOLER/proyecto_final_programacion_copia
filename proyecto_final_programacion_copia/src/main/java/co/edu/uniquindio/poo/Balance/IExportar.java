package co.edu.uniquindio.poo.Balance;

import java.util.ArrayList;
import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Objetos.Persona;

public interface IExportar {
    void exportar(ArrayList<Evento> ListaEventos, ArrayList<Persona> ListaPersonas );
}
