package co.edu.uniquindio.poo.Balance.Excel;

import java.util.ArrayList;

import co.edu.uniquindio.poo.Balance.IExportar;
import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Objetos.Persona;

public class AdapterExcel implements IExportar {

    private static AdapterExcel adapterExcel;

    private AdapterExcel() {
    }

    public static AdapterExcel getInstance() {
        if (adapterExcel == null) {
            adapterExcel = new AdapterExcel();
        }
        return adapterExcel;
    }

    @Override
    public void exportar(ArrayList<Evento> ListaEventos, ArrayList<Persona> ListaPersonas) {
        ToExcel toExcel = new ToExcel();
        toExcel.exportar(ListaEventos, ListaPersonas);
    }
}
