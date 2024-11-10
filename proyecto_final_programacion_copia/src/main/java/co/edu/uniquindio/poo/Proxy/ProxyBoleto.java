package co.edu.uniquindio.poo.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Boleto;

public class ProxyBoleto {
    private static ProxyBoleto instance;

    public static ProxyBoleto getInstance() {
        if (instance == null) {
            instance = new ProxyBoleto();
        }
        return instance;
    }

    public ArrayList<Boleto> getBoletos() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        String query = "SELECT * FROM boleto";
        ArrayList<Boleto> boletos = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String nombreEvento = rs.getString("nombre_evento");
                int idPersona = rs.getInt("id_persona");
                int idEvento = rs.getInt("id_evento");

                Boleto boleto = new Boleto(id, nombre, nombreEvento, idPersona, idEvento);
                boletos.add(boleto);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los boletos: " + e);
        }
        return boletos;
    }
}
