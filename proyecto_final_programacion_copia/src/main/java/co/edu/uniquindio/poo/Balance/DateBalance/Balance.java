package co.edu.uniquindio.poo.Balance.DateBalance;

import java.util.ArrayList;

import co.edu.uniquindio.poo.Balance.Excel.AdapterExcel;
import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Objetos.Persona;
import co.edu.uniquindio.poo.Proxy.ProxyEvento;
import co.edu.uniquindio.poo.Proxy.ProxyPersona;

public class Balance {
    private static Balance balance;
    private ArrayList<Evento> listaEventos = ProxyEvento.getInstance().getEventos();
    private ArrayList<Persona> listaPersonas = ProxyPersona.getInstance().getPersonas();
    private ArrayList<ICalculoBalance> listaSilllas = new ArrayList<>();

    public static Balance getInstance() {
        if (balance == null) {
            balance = new Balance();
            ICalculoBalance sillaNormales = new SillaNormal();
            ICalculoBalance sillaVip = new SillaVip();
            balance.agregarsilla(sillaNormales);
            balance.agregarsilla(sillaVip);
        }
        return balance;
    }

    public void agregarsilla(ICalculoBalance silla) {
        listaSilllas.add(silla);
    }

    // Retorna la informacion del balance balance de sillas de evento(plata recojida
    // por evento)
    /*
     * con el siguiente formato
     * Evento: shakira
     * Sillas Normales: 100.00
     * Sillas VIP: 100.00
     * Total: 200.0
     */
    public ArrayList<String> obtenerBalance() {
        ArrayList<String> listaRecaudos = new ArrayList<>();

        for (Evento event : listaEventos) {
            // Inicializar valores de balance por evento
            double sillaNormal = 0.0;
            double sillaVip = 0.0;
            ArrayList<Persona> listaPersonasVip = new ArrayList<>();
            ArrayList<Persona> listaPersonasNormal = new ArrayList<>();

            StringBuilder cadaTablita = new StringBuilder("Evento: " + event.getNombre() + "\n");

            for (Persona person : listaPersonas) {
                if (person.getIdEvento() == event.getId()) {
                    if (person.getTipoSilla().equals("sillas_vip")) {
                        listaPersonasVip.add(person);
                    } else if (person.getTipoSilla().equals("sillas_regular")) {
                        listaPersonasNormal.add(person);
                    }

                }

            }
            for (ICalculoBalance silla : listaSilllas) {
                if (silla instanceof SillaNormal) {
                    sillaNormal += silla.obtenerBalance(listaPersonasNormal);
                } else if (silla instanceof SillaVip) {
                    sillaVip += silla.obtenerBalance(listaPersonasVip);
                }
            }
            double total = sillaNormal + sillaVip;
            cadaTablita.append("   Sillas Normales: ").append(sillaNormal).append("\n");
            cadaTablita.append("   Sillas VIP: ").append(sillaVip).append("\n");
            cadaTablita.append("   Total: ").append(total).append("\n");

            listaRecaudos.add(cadaTablita.toString());

        }
        return listaRecaudos;

    }

    public void recargarListas() {
        this.listaEventos = ProxyEvento.getInstance().getEventos();
        this.listaPersonas = ProxyPersona.getInstance().getPersonas();
    }

    public void exportar() {
       AdapterExcel.getInstance().exportar(listaEventos, listaPersonas);
    }
}
