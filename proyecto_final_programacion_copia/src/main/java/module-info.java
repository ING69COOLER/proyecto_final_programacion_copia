module co.edu.uniquindio.poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires javafx.graphics;
    requires com.opencsv;
    requires org.apache.poi.ooxml;
    requires com.google.gson;
   
    
  
    
    opens co.edu.uniquindio.poo.Objetos to com.google.gson;
    // Abre los paquetes necesarios para JavaFX
    opens co.edu.uniquindio.poo to javafx.fxml;
    opens co.edu.uniquindio.poo.inicioSesion to javafx.fxml;
    opens co.edu.uniquindio.poo.menuPrincipal to javafx.fxml;
    opens co.edu.uniquindio.poo.registro to javafx.fxml;
    opens co.edu.uniquindio.poo.crear_evento to javafx.fxml;
    opens co.edu.uniquindio.poo.editar_Evento to javafx.fxml;
    opens co.edu.uniquindio.poo.Balance to javafx.fxml;
    opens co.edu.uniquindio.poo.Boleto to javafx.fxml;

    // Exporta los paquetes para su uso fuera del módulo
    exports co.edu.uniquindio.poo;
}
