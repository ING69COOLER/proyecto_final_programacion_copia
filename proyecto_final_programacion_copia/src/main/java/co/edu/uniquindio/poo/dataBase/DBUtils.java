package co.edu.uniquindio.poo.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Objetos.Persona;
import co.edu.uniquindio.poo.Objetos.Usuario;

public class DBUtils {
    //bienvenido(a) al sotano, aca hay clases de agregar evento, usuarios, personas, que ni el credor sabe usar correctamente, buena suerte entendiendo.
    //arroz chino pedido el 3 de noviembre numero por si lo necesita profe : 3206033523

    // Única instancia de la clase (Singleton)
    private static DBUtils instancia;

    // Constructor privado para evitar la creación de nuevas instancias
    private DBUtils() {
        // Constructor vacío, si fuera necesario inicializar algo se haría aquí
    }

    /*
     * Singleton
     * Método estático para obtener la única instancia de la clase
     * 
     */
    public static DBUtils getInstancia() {
        if (instancia == null) {
            instancia = new DBUtils();
        }
        return instancia;
    }

    // Métodos para las operaciones en la base de datos
    public void agregarEvento(Evento evento) {
        String nombre = evento.getNombre();
        int costo = evento.getCosto();
        String tipo = evento.getTipo();
        double porcentajeExtra = evento.getPorcentajeExtra();
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "INSERT INTO Evento (Nombre, Costo, Tipo, porcentaje_extra) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            // Asignar los valores a los parámetros de la consulta
            pstmt.setString(1, nombre);
            pstmt.setInt(2, costo);
            pstmt.setString(3, tipo);
            pstmt.setDouble(4, porcentajeExtra);

            // Ejecutar la consulta
            pstmt.executeUpdate();

            System.out.println("Evento agregado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al agregar evento: " + e);
        }
    }

    public void agregarUsuarios(Usuario usuarios) {
        String user = usuarios.getUsuario();
        String password = usuarios.getContraseña();
        
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "INSERT INTO Usuarios (user, password) VALUES (?, ?)";
    
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            // Asignar los valores a los parámetros de la consulta
            pstmt.setString(1, user);
            pstmt.setString(2, password);
    
            // Ejecutar la consulta
            pstmt.executeUpdate();
    
            System.out.println("Usuario agregado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al agregar usuario: " + e);
        }
    }

    public void agregarPersona(Persona persona) {
        int idPersona = persona.getIdPersona();
        String nombrePersona = persona.getNombrePersona();
        int idEvento = persona.getIdEvento();
        int idSilla = persona.getIdSilla();
        String tipoSilla = persona.getTipoSilla();
        double totalPagar = persona.getTotalPagar();
        
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String queryInsert = "INSERT INTO persona (id_persona, nombre_persona, id_evento, id_silla, tipo_silla, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(queryInsert)) {

                // Asignar los valores a los parámetros de la consulta
                pstmt.setInt(1, idEvento);
                pstmt.setString(2, nombrePersona);
                pstmt.setInt(3, idPersona);
                pstmt.setInt(4, idSilla);
                pstmt.setString(5, tipoSilla);
                pstmt.setDouble(6, totalPagar);
                pstmt.executeUpdate();
        }catch(Exception e){
            System.out.println("Error al agregar persona");
     }
    }
}
