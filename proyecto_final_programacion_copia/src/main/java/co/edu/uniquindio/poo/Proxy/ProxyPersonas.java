package co.edu.uniquindio.poo.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


import co.edu.uniquindio.poo.Objetos.Persona;

public class ProxyPersonas {
    //singleton para el proxy de personas
        private static ProxyPersonas proxyPersonas;
    
        public static ProxyPersonas getInstance() {
            if (proxyPersonas == null) {
                proxyPersonas = new ProxyPersonas();
            }
            return proxyPersonas;
        }
    
        // metodo que retorna un arraylist de personas
        public ArrayList<Persona> getPersonas() {
    
            String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
            String query = "SELECT * FROM Persona ";
            
            // Crear un arraylist de usuarios
            ArrayList<Persona> personas = new ArrayList<>();
    
            try (Connection con = DriverManager.getConnection(url);
                    PreparedStatement pstmt = con.prepareStatement(query)) {
                        
                for (ResultSet rs = pstmt.executeQuery(); rs.next();) {
                    // Crear un objeto usuario y agregarlo al arraylist
                    Persona persona = new Persona(rs.getInt("id_persona"), rs.getString("nombre_persona"), rs.getInt("id_evento"), rs.getInt("id_silla"), rs.getString("tipo_silla"), rs.getDouble("total_pagar"));
                    personas.add(persona);

                    
                }
                    
            } catch (Exception e) {
                System.out.println("Error al verificar si el usuario existe: " + e);
            }
    
            // Retornar el arraylist de usuarios
            return personas;

        

        
    }
}
