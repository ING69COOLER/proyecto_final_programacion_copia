package co.edu.uniquindio.poo.editar_Evento;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uniquindio.poo.Objetos.Persona;
import co.edu.uniquindio.poo.Proxy.ProxyPersonas;
import co.edu.uniquindio.poo.dataBase.DBUtils;

public class EditarEventoController extends BaseEditarEventoController  {

    @FXML
    public void initialize() {
        // Inicialización específica de EditarEventoController si es necesario
    }


    @FXML
    private void guardarCliente() throws SQLException {
        
            if (idPersonaAsignadaEvento()) {
                System.out.println("Error: El id_persona ya está asignado a este evento. No se puede duplicar.");
                return;
            }
            
            int idSilla;
            String tipoSilla;
            try {
                idSilla = obtenerIdSillaSeleccionada();
                tipoSilla = obtenerTipoSillaSeleccionada();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }

            if (isSillaAsignada(idSilla, tipoSilla)) {
                System.out.println("Error: La combinación de evento, silla y tipo ya está asignada.");
                return;
            }

            insertarCliente( idSilla, tipoSilla);
            cerrarVentana();
       
    }
    //si el id_persona ya esta asignado a este evento, no se puede duplicar, metodo del controlador
    @Override
    protected boolean idPersonaAsignadaEvento() {
    ArrayList<Persona> personas = ProxyPersonas.getInstance().getPersonas();
    int id_persona_asignado = Integer.parseInt(txtIdPersona.getText());

    for (Persona persona : personas) {
        if (persona.getIdPersona() == id_persona_asignado) {
            System.out.println("Error: El id_persona ya está asignado a este evento. No se puede duplicar.");
            return true;  
           
        }
    }
    return false;  
}

    public void cargarDatosEvento(int idEvento) {
        this.idEvento = idEvento;
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        
        try (Connection con = DriverManager.getConnection(url)) {
            cargarDetallesEvento(con);
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_regular", gridSillas, "sillas_regular");
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_vip", gridSillasVip, "sillas_vip");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos del evento: " + e);
        }
    }

    private int obtenerIdSillaSeleccionada() {
        RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
        if (sillaSeleccionada != null) {
            if (gridSillas.getChildren().contains(sillaSeleccionada)) {
                return gridSillas.getChildren().indexOf(sillaSeleccionada) + 1;
            } else if (gridSillasVip.getChildren().contains(sillaSeleccionada)) {
                return gridSillasVip.getChildren().indexOf(sillaSeleccionada) + 1;
            }
        }
        throw new IllegalStateException("Error: No se ha seleccionado ninguna silla.");
    }

    private String obtenerTipoSillaSeleccionada() {
        RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
        if (sillaSeleccionada != null) {
            if (gridSillas.getChildren().contains(sillaSeleccionada)) {
                return "sillas_regular";
            } else if (gridSillasVip.getChildren().contains(sillaSeleccionada)) {
                return "sillas_vip";
            }
        }
        throw new IllegalStateException("Error: No se ha seleccionado ninguna silla.");
    }

   private boolean isSillaAsignada( int idSilla, String tipoSilla) {
    ArrayList<Persona> personas = ProxyPersonas.getInstance().getPersonas();
    for (Persona persona : personas) {
        if (persona.getIdSilla() == idSilla && persona.getTipoSilla() == tipoSilla && persona.getIdEvento() == idEvento) {
            return true;
        }
    }
    return false;
    
}


    private void insertarCliente( int idSilla, String tipoSilla)  {
        String idPersonaText = txtIdPersona.getText().trim();
        String nombrePersona = txtNombrePersona.getText().trim();
    
        if (idPersonaText.isEmpty()) {
            // Muestra un mensaje de error o maneja el caso en el que el campo esté vacío
            System.out.println("Error: El campo de ID de persona está vacío.");
            return;
        }
    
        if (nombrePersona.isEmpty()) {
            System.out.println("Error: El campo de nombre de persona está vacío.");
            return;
        }
    
        int idPersona = Integer.parseInt(idPersonaText); // Conversión segura después de la verificación
        double totalPagar = calcularTotalPagar();
        
        DBUtils.getInstancia().agregarPersona(new Persona(idEvento, nombrePersona, idPersona, idSilla, tipoSilla, totalPagar));
    
    }
    

}
