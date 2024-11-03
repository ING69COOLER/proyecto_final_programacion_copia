package co.edu.uniquindio.poo.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Evento;


public class ProxyEvento {
    // aca iria el crud de eventos, pero se agregara mas adelante

    private static ProxyEvento proxyEvento;

    public static ProxyEvento getInstance() {
        if (proxyEvento == null) {
            proxyEvento = new ProxyEvento();
        }
        return proxyEvento;
    }   

     public ArrayList<Evento> getEventos() {
         String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
         ArrayList<Evento> eventos = new ArrayList<>();


        try {
            Connection con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Evento");

            while (rs.next()) {
                int id = rs.getInt("Id");
                String nombre = rs.getString("Nombre");
                int costo = rs.getInt("Costo");
                String tipo = rs.getString("Tipo");
                double porcentajeExtra = rs.getDouble("porcentaje_extra");
                Evento evento = new Evento(id, nombre, costo, tipo, porcentajeExtra);
                eventos.add(evento);
            }

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error al cargar los eventos: " + e);
        }
        return eventos;
    }
}
