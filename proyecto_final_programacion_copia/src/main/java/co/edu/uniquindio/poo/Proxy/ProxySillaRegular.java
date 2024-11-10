package co.edu.uniquindio.poo.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList; 

import co.edu.uniquindio.poo.Objetos.SillaRegular;

public class ProxySillaRegular {
    private static ProxySillaRegular instance;

    public static ProxySillaRegular getInstance() {
        if (instance == null) {
            instance = new ProxySillaRegular();
        }
        return instance;
    }

    public ArrayList<SillaRegular> getSillasRegular() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        String query = "SELECT * FROM sillas_regular";
        ArrayList<SillaRegular> sillasRegular = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                SillaRegular sillaRegular = new SillaRegular(id, nombre);
                sillasRegular.add(sillaRegular);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las sillas: " + e);
        }
        return sillasRegular;
    }
}
