package co.edu.uniquindio.poo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Usuario;
import co.edu.uniquindio.poo.Proxy.ProxyUsuario;
import co.edu.uniquindio.poo.dataBase.DBUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import co.edu.uniquindio.poo.Objetos.Evento;

public  interface  Utils {
    static String clave_empresarial = "1234";

    public default String getClave_empresarial() {
        return clave_empresarial;

        
    }

    public default  boolean usuarioExiste(String usuario) {
        // Verificar si el usuario existe en la base de datos
        ArrayList<Usuario> usuarios = ProxyUsuario.getInstance().getUsuarios();
        for (Usuario user : usuarios) {
            // Verificar si el usuario existe en la lista de usuarios
            if (user.getUsuario().equals(usuario)) {
                return false;
            }
        }
        return true;
    }

    public default  boolean verificarClave(String usuario, String clave) {
                String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        boolean coincide = false; // Variable para almacenar el resultado

        try {
            // Conectar a la base de datos
            Connection con = DriverManager.getConnection(url);
            // Usar PreparedStatement para evitar inyecciones SQL
            String sql = "SELECT * FROM Usuarios WHERE user = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, usuario); // Establecer el usuario
            pstmt.setString(2, clave);    // Establecer la clave

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Verificar si hay algún resultado
            if (rs.next()) {
                coincide = true; // Hay coincidencia
            }

            // Cerrar los recursos
            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error al verificar la clave: " + e);
        }

        return coincide; // Retornar el resultado
    }

    public default void crearUsuario(String usuario, String contraseña) {
           DBUtils.getInstancia().agregarUsuarios(new Usuario(usuario, contraseña));
        }

    public default void crearEvento(int id, String nombre, int costo, String tipo, double porcentajeExtra) {
        DBUtils.getInstancia().agregarEvento(new Evento(id,nombre, costo, tipo, porcentajeExtra));
    }

    public default void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
