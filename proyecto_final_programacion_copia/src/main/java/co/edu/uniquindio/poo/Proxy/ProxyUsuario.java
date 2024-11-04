package co.edu.uniquindio.poo.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Usuario;

public class ProxyUsuario {
    private static ProxyUsuario proxyUsuario;

    public static ProxyUsuario getInstance() {
        if (proxyUsuario == null) {
            proxyUsuario = new ProxyUsuario();
        }
        return proxyUsuario;
    }

    public ArrayList<Usuario> getUsuarios() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "SELECT * FROM Usuarios"; // Eliminamos el filtro WHERE user = ?

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { // Cambiamos a while para recorrer todos los registros
                Usuario usuario = new Usuario(rs.getString("user"), rs.getString("password"));
                usuarios.add(usuario);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener los usuarios: " + e);
        }

        return usuarios;
    }
}

