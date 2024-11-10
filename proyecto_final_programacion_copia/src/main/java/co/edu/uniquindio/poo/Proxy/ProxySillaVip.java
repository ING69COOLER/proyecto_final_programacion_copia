package co.edu.uniquindio.poo.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.SillaVip;

public class ProxySillaVip {
    private static ProxySillaVip instance;

    public static ProxySillaVip getInstance() {
        if (instance == null) {
            instance = new ProxySillaVip();
        }
        return instance;
    }

    public ArrayList<SillaVip> getSillasVip() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        String query = "SELECT * FROM sillas_vip";
        ArrayList<SillaVip> sillasVip = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                SillaVip sillaVip = new SillaVip(id, nombre);
                sillasVip.add(sillaVip);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las sillas: " + e);
        }
        return sillasVip;
    }
}
