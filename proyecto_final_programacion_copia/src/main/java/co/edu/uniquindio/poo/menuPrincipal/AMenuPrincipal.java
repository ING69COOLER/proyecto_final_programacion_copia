package co.edu.uniquindio.poo.menuPrincipal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import co.edu.uniquindio.poo.Utils;
import co.edu.uniquindio.poo.editar_Evento.BaseEditarEventoController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public abstract class AMenuPrincipal implements Utils {
     //Se va
    public void editarEvento(int idEvento) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/editar_evento.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el ID del evento
            BaseEditarEventoController controller = loader.getController();
            controller.cargarDatosEvento(idEvento); // Pasar el ID del evento
            System.out.println("Evento cargado "+idEvento);

            // Configurar y mostrar la ventana de edición
            Stage stage = new Stage();
            stage.setTitle("Editar Evento");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 
    protected String obtenerResumenEvento(Connection con, int idEvento, String nombreEvento) throws SQLException {
        int sillasVip = contarSillasVip(con, idEvento);
        int sillasRegulares = contarSillasRegulares(con, idEvento);

        return String.format("Evento: %s\n  Sillas Regulares: %d\n  Sillas VIP: %d\n\n",
                nombreEvento, sillasRegulares, sillasVip);
    }

    
    private int contarSillasVip(Connection con, int idEvento) throws SQLException {
        String vipQuery = "SELECT COUNT(*) FROM sillas_vip WHERE id NOT IN " +
                "(SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = 'sillas_vip')";
        try (PreparedStatement pstmtVip = con.prepareStatement(vipQuery)) {
            pstmtVip.setInt(1, idEvento);
            ResultSet rsVip = pstmtVip.executeQuery();
            rsVip.next();
            return rsVip.getInt(1);
        }
    }

    
    private int contarSillasRegulares(Connection con, int idEvento) throws SQLException {
        String regularQuery = "SELECT COUNT(*) FROM sillas_regular WHERE id NOT IN " +
                "(SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = 'sillas_regular')";
        try (PreparedStatement pstmtRegular = con.prepareStatement(regularQuery)) {
            pstmtRegular.setInt(1, idEvento);
            ResultSet rsRegular = pstmtRegular.executeQuery();
            rsRegular.next();
            return rsRegular.getInt(1);
        }
    }

    // Mostrar diálogo para ingresar nombre del evento y clave empresarial
    protected Optional<Pair<String, String>> mostrarDialogoEliminarEvento() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Eliminar Evento");
        dialog.setHeaderText("Ingrese el nombre del evento y la clave empresarial para eliminarlo");

        ButtonType eliminarButtonType = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(eliminarButtonType, ButtonType.CANCEL);

        TextField txtNombreEvento = new TextField();
        txtNombreEvento.setPromptText("Nombre del evento");
        PasswordField txtClaveEmpresarial = new PasswordField();
        txtClaveEmpresarial.setPromptText("Clave empresarial");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Nombre del evento:"), 0, 0);
        grid.add(txtNombreEvento, 1, 0);
        grid.add(new Label("Clave empresarial:"), 0, 1);
        grid.add(txtClaveEmpresarial, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Node eliminarButton = dialog.getDialogPane().lookupButton(eliminarButtonType);
        eliminarButton.setDisable(true);

        txtNombreEvento.textProperty().addListener((observable, oldValue, newValue) -> {
            eliminarButton.setDisable(newValue.trim().isEmpty() || txtClaveEmpresarial.getText().trim().isEmpty());
        });
        txtClaveEmpresarial.textProperty().addListener((observable, oldValue, newValue) -> {
            eliminarButton.setDisable(newValue.trim().isEmpty() || txtNombreEvento.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == eliminarButtonType) {
                return new Pair<>(txtNombreEvento.getText(), txtClaveEmpresarial.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

  
    // Validar la clave empresarial ingresada
    protected boolean validarClaveEmpresarial(String claveEmpresarial) {
        return claveEmpresarial.equals(getClave_empresarial());
    }

   
    // Eliminar las personas asociadas y el evento de la base de datos
    protected boolean eliminarPersonasYEvento(String nombreEvento) {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";


        try (Connection con = DriverManager.getConnection(url)) {
            // Primero eliminar las personas relacionadas con el evento
            String queryEliminarPersonas = "DELETE FROM persona WHERE id_evento = (SELECT Id FROM Evento WHERE Nombre = ?)";
            String queryEliminarBoletos = "DELETE FROM boleto WHERE id_evento = (SELECT Id FROM Evento WHERE Nombre = ?)";
            try (PreparedStatement psEliminarPersonas = con.prepareStatement(queryEliminarPersonas)) {
                psEliminarPersonas.setString(1, nombreEvento);
                int personasEliminadas = psEliminarPersonas.executeUpdate();
                System.out.println("Se eliminaron " + personasEliminadas + " personas relacionadas con el evento.");
            }
            try (PreparedStatement psEliminarBoletos = con.prepareStatement(queryEliminarBoletos)) {
                psEliminarBoletos.setString(1, nombreEvento);
                int boletosEliminados = psEliminarBoletos.executeUpdate();
                System.out.println("Se eliminaron " + boletosEliminados + " boletos relacionados con el evento.");
            }
       


            // Luego eliminar el evento
            String queryEliminarEvento = "DELETE FROM Evento WHERE Nombre = ?";
            try (PreparedStatement psEliminarEvento = con.prepareStatement(queryEliminarEvento)) {
                psEliminarEvento.setString(1, nombreEvento);
                int eventosEliminados = psEliminarEvento.executeUpdate();
                if (eventosEliminados > 0) {
                    System.out.println("El evento '" + nombreEvento + "' fue eliminado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el evento con el nombre: " + nombreEvento);
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el evento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



   
}