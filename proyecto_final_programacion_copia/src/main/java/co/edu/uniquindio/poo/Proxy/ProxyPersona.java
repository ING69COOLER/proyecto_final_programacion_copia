package co.edu.uniquindio.poo.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Persona;

public class ProxyPersona {
    // Singleton para el proxy de personas
    private static ProxyPersona proxyPersonas;

    public static ProxyPersona getInstance() {
        if (proxyPersonas == null) {
            proxyPersonas = new ProxyPersona();
        }
        return proxyPersonas;
    }

    // Método que retorna un ArrayList de personas
    public ArrayList<Persona> getPersonas() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "SELECT * FROM Persona";
        
        // Crear un ArrayList de personas
        ArrayList<Persona> personas = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Crear un objeto Persona y agregarlo al ArrayList
                Persona persona = new Persona(
                    rs.getInt("id_persona"),
                    rs.getString("nombre_persona"),
                    rs.getInt("id_evento"),
                    rs.getInt("id_silla"),
                    rs.getString("tipo_silla"),
                    rs.getDouble("total_pagar")
                );
                personas.add(persona);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener las personas: " + e);
        }

        return personas;
    }
}
