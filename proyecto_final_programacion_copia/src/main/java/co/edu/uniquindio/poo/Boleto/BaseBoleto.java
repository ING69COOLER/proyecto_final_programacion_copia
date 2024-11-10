package co.edu.uniquindio.poo.Boleto;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


import co.edu.uniquindio.poo.Objetos.Persona;

import co.edu.uniquindio.poo.Proxy.ProxyPersona;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import co.edu.uniquindio.poo.Objetos.SillaRegular;
import co.edu.uniquindio.poo.Objetos.SillaVip;
import co.edu.uniquindio.poo.Proxy.ProxySillaRegular;
import co.edu.uniquindio.poo.Proxy.ProxySillaVip;
import co.edu.uniquindio.poo.Utils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class BaseBoleto implements Utils {
    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected Button btnEliminar;

    @FXML
    protected Button btnImprimir;

    @FXML
    protected Label txtCedula;

    @FXML
    protected Label txtEvento;

    @FXML
    protected Label txtNombre;

    @FXML
    protected Label txtTipoSilla;

    @FXML
    protected Label txtTotal;

    String nombreCliente;
    String idPersona;
    String nombreEvento;
    int idEvento;
    String tipoSilla;
    double totalPagar;

    public void cargarDatosBoleto(int idEvento, int idPersona, String nombreEvento) {
        this.idEvento = idEvento;
        this.idPersona = String.valueOf(idPersona);
        this.nombreEvento = nombreEvento;

        ArrayList<SillaRegular> sillasRegular = ProxySillaRegular.getInstance().getSillasRegular();
        ArrayList<SillaVip> sillasVip = ProxySillaVip.getInstance().getSillasVip();
        ArrayList<Persona> personas = ProxyPersona.getInstance().getPersonas();
        for (Persona persona : personas) {
            if (persona.getIdPersona() == idPersona && persona.getIdEvento() == idEvento) {
                nombreCliente = persona.getNombrePersona();
                totalPagar = persona.getTotalPagar();
                tipoSilla = persona.getTipoSilla() + "\n ";
                for (SillaRegular silla : sillasRegular) {
                    if (silla.getId() == persona.getIdSilla() && persona.getTipoSilla().equals("sillas_regular")) {
                        tipoSilla += "Puesto:" + silla.getNombre();
                    }

                }
                for (SillaVip silla : sillasVip) {
                    if (silla.getId() == persona.getIdSilla() && persona.getTipoSilla().equals("sillas_vip")) {
                        tipoSilla += "Puesto: " + silla.getNombre();
                    }
                }
            }

        }
        String cedula = String.valueOf(idPersona);

        txtNombre.setText(nombreCliente);
        txtCedula.setText(cedula);
        txtEvento.setText(nombreEvento);
        txtTipoSilla.setText(tipoSilla);
        txtTotal.setText(String.valueOf(totalPagar));

    }

    protected Optional<String> mostrarDialogoEliminarEvento() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Eliminar Boleto");
        dialog.setHeaderText("Ingrese la clave empresarial para eliminarlo");

        ButtonType eliminarButtonType = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(eliminarButtonType, ButtonType.CANCEL);

        PasswordField txtClaveEmpresarial = new PasswordField();
        txtClaveEmpresarial.setPromptText("Clave empresarial");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Clave empresarial:"), 0, 0);
        grid.add(txtClaveEmpresarial, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Node eliminarButton = dialog.getDialogPane().lookupButton(eliminarButtonType);
        eliminarButton.setDisable(true);

        // Deshabilitar el botón "Eliminar" si el campo de clave empresarial está vacío
        txtClaveEmpresarial.textProperty().addListener((observable, oldValue, newValue) -> {
            eliminarButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == eliminarButtonType) {
                return txtClaveEmpresarial.getText();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    protected boolean validarClaveEmpresarial(String claveEmpresarial) {
        return claveEmpresarial.equals(getClave_empresarial());
    }

    protected void eliminarBoletoYPersona(int eliminar_idEvento, String eliminar_id_persona) {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        // Iniciar la conexión y la transacción
        try (Connection con = DriverManager.getConnection(url)) {
            con.setAutoCommit(false); // Desactivar el autocommit para manejar la transacción

            // Eliminar el boleto
            String queryEliminarBoleto = "DELETE FROM boleto WHERE id_evento = ? AND id_persona = ?";
            try (PreparedStatement psEliminarBoleto = con.prepareStatement(queryEliminarBoleto)) {
                psEliminarBoleto.setInt(1, eliminar_idEvento);
                psEliminarBoleto.setString(2, eliminar_id_persona);

                int boletosEliminados = psEliminarBoleto.executeUpdate();
                if (boletosEliminados > 0) {
                    System.out.println("Se eliminaron " + boletosEliminados + " boletos relacionados con el evento.");
                } else {
                    System.out.println("No se encontraron boletos para eliminar.");
                }
            }

            // Eliminar la persona relacionada con el evento
            String queryEliminarPersona = "DELETE FROM persona WHERE id_persona = ? AND id_evento = ?";
            try (PreparedStatement psEliminarPersona = con.prepareStatement(queryEliminarPersona)) {
                psEliminarPersona.setString(1, eliminar_id_persona);
                psEliminarPersona.setInt(2, eliminar_idEvento);

                int personasEliminadas = psEliminarPersona.executeUpdate();
                if (personasEliminadas > 0) {
                    System.out.println(
                            "Se eliminó a la persona con ID " + eliminar_id_persona + " relacionada con el evento.");
                } else {
                    System.out.println("No se encontró la persona para eliminar.");
                }
            }

            // Confirmar la transacción
            con.commit();
            System.out.println("Eliminación completada con éxito.");

        } catch (SQLException e) {
            // Si ocurre algún error, se revierte la transacción
            System.out.println("Error durante la eliminación: " + e.getMessage());
            try (Connection con = DriverManager.getConnection(url)) {
                con.rollback(); // Deshacer los cambios
            } catch (SQLException rollbackEx) {
                System.out.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }
        }
    }

    protected void regresar() throws IOException {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        
        stage.close();
    }

}
