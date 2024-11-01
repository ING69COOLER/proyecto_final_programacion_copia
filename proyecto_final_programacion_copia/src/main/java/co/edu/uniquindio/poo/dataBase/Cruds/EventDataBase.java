package co.edu.uniquindio.poo.dataBase.Cruds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class EventDataBase implements IMBD {

    @Override
    public void createTable() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try {
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            smt.executeUpdate("CREATE TABLE \"Evento\" (\r\n" + //
                    "\t\"Id\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                    "\t\"Nombre\"\tTEXT NOT NULL,\r\n" + //
                    "\t\"Costo\"\tINTEGER NOT NULL,\r\n" + //
                    "\t\"Tipo\"\tTEXT NOT NULL,\r\n" + //
                    "\t\"porcentaje_extra\"\tREAL NOT NULL,\r\n" + //
                    "\tPRIMARY KEY(\"Id\" AUTOINCREMENT)\r\n" + //
                    ");");
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");
        } catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
       
    }
    

}
