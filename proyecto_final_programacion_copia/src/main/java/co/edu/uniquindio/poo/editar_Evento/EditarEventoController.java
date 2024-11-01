package co.edu.uniquindio.poo.editar_Evento;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditarEventoController extends BaseEditarEventoController  {

    @FXML
    public void initialize() {
        // Inicialización específica de EditarEventoController si es necesario
    }


    @FXML
    private void guardarCliente() {
        try (Connection con = connectDatabase()) {
            if (idPersonaAsignadaEvento(con)) {
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

            if (combinacionSillaYaAsignada(con, idSilla, tipoSilla)) {
                System.out.println("Error: La combinación de evento, silla y tipo ya está asignada.");
                return;
            }

            insertarCliente(con, idSilla, tipoSilla);
            cerrarVentana();
        } catch (Exception e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean idPersonaAsignadaEvento(Connection con) throws SQLException {
        String queryCheckIdEvento = "SELECT COUNT(*) FROM persona WHERE id_persona = ? AND id_evento = ?";
        try (PreparedStatement psCheckIdEvento = con.prepareStatement(queryCheckIdEvento)) {
            psCheckIdEvento.setInt(1, Integer.parseInt(txtIdPersona.getText()));
            psCheckIdEvento.setInt(2, idEvento);
            try (ResultSet rsCheckIdEvento = psCheckIdEvento.executeQuery()) {
                rsCheckIdEvento.next();
                return rsCheckIdEvento.getInt(1) > 0;
            }
        }
    }

    public void cargarDatosEvento(int idEvento) {
        this.idEvento = idEvento;
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        
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

    private boolean combinacionSillaYaAsignada(Connection con, int idSilla, String tipoSilla) throws SQLException {
        String queryCheck = "SELECT COUNT(*) FROM persona WHERE id_evento = ? AND id_silla = ? AND tipo_silla = ?";
        try (PreparedStatement psCheck = con.prepareStatement(queryCheck)) {
            psCheck.setInt(1, idEvento);
            psCheck.setInt(2, idSilla);
            psCheck.setString(3, tipoSilla);
            try (ResultSet rsCheck = psCheck.executeQuery()) {
                rsCheck.next();
                return rsCheck.getInt(1) > 0;
            }
        }
    }

    private void insertarCliente(Connection con, int idSilla, String tipoSilla) throws SQLException {
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
    
        String queryInsert = "INSERT INTO persona (id_persona, nombre_persona, id_evento, id_silla, tipo_silla, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psInsert = con.prepareStatement(queryInsert)) {
            psInsert.setInt(1, idPersona);
            psInsert.setString(2, nombrePersona);
            psInsert.setInt(3, idEvento);
            psInsert.setInt(4, idSilla);
            psInsert.setString(5, tipoSilla);
            psInsert.setDouble(6, totalPagar);
            psInsert.executeUpdate();
        }
    }
    

}
