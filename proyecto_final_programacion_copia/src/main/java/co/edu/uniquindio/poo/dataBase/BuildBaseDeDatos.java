package co.edu.uniquindio.poo.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildBaseDeDatos implements IBuildBaseDeDatos {

    private static BuildBaseDeDatos instancia;

    /*
     * Singleton
     */
    public static BuildBaseDeDatos getInstancia() {
        if (instancia == null) {
            instancia = new BuildBaseDeDatos();
        }
        return instancia;
    }

    public void crearSillasVip() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";


        try (Connection con = DriverManager.getConnection(url);
                Statement smt = con.createStatement()) {

            // Crear la tabla sillas_vip si no existe
            smt.executeUpdate("CREATE TABLE IF NOT EXISTS \"sillas_vip\" (\r\n" +
                    "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" +
                    "\t\"nombre\"\tTEXT NOT NULL,\r\n" + // Cambiado a TEXT para las letras
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" +
                    ");");

            // Verificar si ya existen registros en la tabla
            String countQuery = "SELECT COUNT(*) FROM sillas_vip";
            try (Statement stmtCount = con.createStatement();
                    ResultSet rsCount = stmtCount.executeQuery(countQuery)) {

                rsCount.next();
                int count = rsCount.getInt(1);

                if (count > 0) {
                    System.out.println("Las sillas VIP ya están creadas. No se insertarán nuevas.");
                    return; // Salir si ya hay registros
                }
            }

            // Insertar sillas VIP si no hay registros
            String insertQuery = "INSERT INTO sillas_vip (nombre) VALUES (?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                for (int i = 0; i < 10; i++) {
                    char letra = (char) ('A' + (i % 26)); // Obtener una letra del alfabeto (se repiten después de 'Z')
                    pstmt.setString(1, String.valueOf(letra)); // Asignar la letra de silla
                    pstmt.executeUpdate();
                }
            }

            System.out.println("Sillas VIP insertadas exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al crear o insertar sillas VIP: " + e);
        }
    }

    public void crearSillasRegulares() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";


        try (Connection con = DriverManager.getConnection(url);
                Statement smt = con.createStatement()) {

            // Crear la tabla sillas si no existe
            smt.executeUpdate("CREATE TABLE IF NOT EXISTS \"sillas_regular\" (\r\n" +
                    "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" +
                    "\t\"nombre\"\tTEXT NOT NULL,\r\n" + // Cambiado a TEXT para las letras
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" +
                    ");");

            // Verificar si ya existen registros en la tabla
            String countQuery = "SELECT COUNT(*) FROM sillas_regular";
            try (Statement stmtCount = con.createStatement();
                    ResultSet rsCount = stmtCount.executeQuery(countQuery)) {

                rsCount.next();
                int count = rsCount.getInt(1);

                if (count > 0) {
                    System.out.println("Las sillas regulares ya están creadas. No se insertarán nuevas.");
                    return; // Salir si ya hay registros
                }
            }

            // Insertar sillas regulares si no hay registros
            String insertQuery = "INSERT INTO sillas_regular (nombre) VALUES (?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                for (int i = 0; i < 10; i++) {
                    char letra = (char) ('A' + (i % 26)); // Obtener una letra del alfabeto (se repiten después de 'Z')
                    pstmt.setString(1, String.valueOf(letra)); // Asignar la letra de silla
                    pstmt.executeUpdate();
                }
            }

            System.out.println("Sillas regulares insertadas exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al crear o insertar sillas regulares: " + e);
        }
    }

    public void crearTablas() {
        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (\r\n" +
                "\t\tid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\r\n" +
                "\t\tuser TEXT NOT NULL,\r\n" +
                "\t\tpassword TEXT NOT NULL\r\n" +
                ");";

        String tablaEvento = "CREATE TABLE IF NOT EXISTS evento (\r\n" +
                "\t\tid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\r\n" +
                "\t\tnombre TEXT NOT NULL,\r\n" +
                "\t\tcosto INTEGER NOT NULL,\r\n" +
                "\t\ttipo TEXT NOT NULL,\r\n" +
                "\t\tporcentaje_extra REAL NOT NULL\r\n" +
                ");";

        String tablaPersona = "CREATE TABLE IF NOT EXISTS persona (\r\n" +
                "\t\tid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\r\n" +
                "\t\tid_evento INTEGER NOT NULL,\r\n" +
                "\t\tid_silla INTEGER NOT NULL,\r\n" +
                "\t\ttipo_silla TEXT NOT NULL,\r\n" +
                "\t\tnombre_persona TEXT NOT NULL,\r\n" +
                "\t\tid_persona INTEGER NOT NULL,\r\n" +
                "\t\ttotal_pagar INTEGER NOT NULL\r\n" +
                ");";

        String tablaBoleto = "CREATE TABLE IF NOT EXISTS boleto (\r\n" +
                "\t\"id\" INTEGER NOT NULL UNIQUE,\r\n" +
                "\t\"nombre\" TEXT NOT NULL,\r\n" +
                "\t\"nombre_evento\" TEXT NOT NULL,\r\n" +
                "\t\"id_persona\" INTEGER NOT NULL,\r\n" +
                "\t\"id_evento\" INTEGER NOT NULL,\r\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" +
                ");";

        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url);
                Statement smt = con.createStatement()) {

            // Verificar si las tablas existen
            boolean usuariosExistente = false;
            boolean eventoExistente = false;
            boolean personaExistente = false;
            boolean boletoExistente = false;

            // Verificar existencia de cada tabla
            ResultSet rsUsuarios = smt
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='usuarios';");
            if (rsUsuarios.next()) {
                usuariosExistente = true;
            }

            ResultSet rsEvento = smt
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='evento';");
            if (rsEvento.next()) {
                eventoExistente = true;
            }

            ResultSet rsPersona = smt
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='persona';");
            if (rsPersona.next()) {
                personaExistente = true;
            }

            ResultSet rsBoleto = smt
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='boleto';");
            if (rsBoleto.next()) {
                boletoExistente = true;
            }

            // Si alguna de las tablas no existe, las creamos
            if (usuariosExistente && eventoExistente && personaExistente) {
                System.out.println("Las tablas ya existen.");
            } else {
                if (!usuariosExistente)
                    smt.execute(tablaUsuarios);
                if (!eventoExistente)
                    smt.execute(tablaEvento);
                if (!personaExistente)
                    smt.execute(tablaPersona);
                if (!boletoExistente)
                    smt.execute(tablaBoleto);

                System.out.println("Tablas creadas exitosamente.");
            }
            crearSillasRegulares();
            crearSillasVip();

        } catch (Exception e) {
            System.out.println("Error al crear tablas: " + e);
        }
    }

    public void eliminarEvento(String nombreEvento) throws SQLException {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        Connection con = DriverManager.getConnection(url);

        String queryEliminarEvento = "DELETE FROM Evento WHERE Nombre = ?";
        try (PreparedStatement psEliminarEvento = con.prepareStatement(queryEliminarEvento)) {
            psEliminarEvento.setString(1, nombreEvento);
            int eventosEliminados = psEliminarEvento.executeUpdate();
            if (eventosEliminados > 0) {
                System.out.println("El evento '" + nombreEvento + "' fue eliminado correctamente.");
               
            } else {
                System.out.println("No se encontró el evento con el nombre: " + nombreEvento);
                
            }
        }
        
    }
}