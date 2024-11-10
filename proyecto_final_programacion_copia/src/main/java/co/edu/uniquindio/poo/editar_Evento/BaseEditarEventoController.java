package co.edu.uniquindio.poo.editar_Evento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import co.edu.uniquindio.poo.Boleto.BoletoController;
import co.edu.uniquindio.poo.Objetos.Boleto;
import co.edu.uniquindio.poo.Proxy.ProxyBoleto;

public abstract class BaseEditarEventoController {

    @FXML
    protected Label lblNombreEvento, lblCostoEvento, lblTotalPagar;

    @FXML
    protected GridPane gridSillas, gridSillasVip;

    @FXML
    protected VBox panelBoletos;

    @FXML
    protected TextField txtNombrePersona, txtIdPersona;

    protected int idEvento;
    protected double costoRegular;
    protected double porcentajeExtra;

    protected ToggleGroup toggleGroupSillas = new ToggleGroup();

    protected Connection connectDatabase() throws SQLException {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        return DriverManager.getConnection(url);
    }

    public int getIdEvento() {
        return idEvento;
    }

    

    protected void cargarDetallesEvento(Connection con) throws SQLException {
        String queryEvento = "SELECT Nombre, Costo, porcentaje_extra FROM Evento WHERE Id = ?";
        try (PreparedStatement psEvento = con.prepareStatement(queryEvento)) {
            psEvento.setInt(1, idEvento);
            try (ResultSet rsEvento = psEvento.executeQuery()) {
                if (rsEvento.next()) {
                    lblNombreEvento.setText(rsEvento.getString("Nombre"));
                    costoRegular = rsEvento.getDouble("Costo");
                    porcentajeExtra = rsEvento.getDouble("porcentaje_extra");
                    lblCostoEvento.setText("Costo: $" + costoRegular);
                }
            }
        }
    }

    protected void cargarSillasDisponibles(String query, GridPane grid, String tipoSillaQuery) {
        try (Connection con = connectDatabase()) {
            Set<Integer> sillasOcupadas = obtenerSillasOcupadas(con, tipoSillaQuery);
            cargarSillasEnGrid(con, query, grid, sillasOcupadas);
        } catch (Exception e) {
            System.out.println("Error al cargar las sillas: " + e);
        }
    }

    protected Set<Integer> obtenerSillasOcupadas(Connection con, String tipoSillaQuery) throws SQLException {
        String querySillasOcupadas = "SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = ?";
        try (PreparedStatement psOcupadas = con.prepareStatement(querySillasOcupadas)) {
            psOcupadas.setInt(1, idEvento);
            psOcupadas.setString(2, tipoSillaQuery);
            ResultSet rsOcupadas = psOcupadas.executeQuery();

            Set<Integer> sillasOcupadas = new HashSet<>();
            while (rsOcupadas.next()) {
                int idSilla = rsOcupadas.getInt("id_silla");
                sillasOcupadas.add(idSilla);
            }
            return sillasOcupadas;
        }
    }

    protected void cargarSillasEnGrid(Connection con, String query, GridPane grid, Set<Integer> sillasOcupadas)
            throws SQLException {
        try (PreparedStatement psSillas = con.prepareStatement(query); ResultSet rsSillas = psSillas.executeQuery()) {
            int row = 0, col = 0;
            while (rsSillas.next()) {
                int idSilla = rsSillas.getInt("id");
                String nombreSilla = rsSillas.getString("nombre");
                RadioButton silla = crearBotonSilla(nombreSilla, idSilla, sillasOcupadas);

                grid.add(silla, col++, row);
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public void cargarDatosEvento(int idEvento) {
        this.idEvento = idEvento;
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url)) {
            cargarDetallesEvento(con);
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_regular", gridSillas, "sillas_regular");
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_vip", gridSillasVip, "sillas_vip");
            cargarBoletos();
        } catch (Exception e) {
            System.out.println("Error al cargar los datos del evento: " + e);
        }
    }

    protected void cargarBoletos() {
        System.out.println("Cargando boletos..." + idEvento);
        ArrayList<Boleto> boletos = ProxyBoleto.getInstance().getBoletos();
        panelBoletos.getChildren().clear();
        for (Boleto boleto : boletos) {
            System.out.println(
                    "Boleto: " + boleto.getNombre() + ", ID: " + boleto.getIdEvento() + " Id evento: " + getIdEvento());
            if (boleto.getIdEvento() == getIdEvento()) {
                String nombreCliente = boleto.getNombre();
                int idPersona = boleto.getIdPersona();
                String nombreBoton = "Nombre: " + nombreCliente + "\n" + "ID: " + idPersona;
                System.out.println(nombreBoton);
                Button boton = new Button(nombreBoton);
                boton.setStyle(
                        "-fx-background-color: linear-gradient(yellow, #FF8C00);" + // Degradado de blanco a naranja
                                                                                    // oscuro
                                "-fx-text-fill: black;" + // Color de texto blanco
                                "-fx-font-family: 'Forte';" + // Fuente "Forte"
                                "-fx-background-radius: 20;" + // Bordes redondeados
                                "-fx-padding: 10px 20px;" + // Espaciado interno (alto y ancho)
                                "-fx-font-size: 10px;" + // Tamaño de fuente
                                "-fx-border-radius: 20;" // Bordes redondeados en el borde exterior
                );

                boton.setPrefWidth(200);
                boton.setOnAction(e -> mostrarDetallesBoleto(idEvento, idPersona));

                // Agregar el botón al VBox
                panelBoletos.getChildren().add(boton);

                // Crear un botón para cada boleto
            }
        }
    }

    protected void mostrarDetallesBoleto(int idEvento, int idPersona) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/boleto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el ID del evento
            BoletoController controller = loader.getController();
            String nombreEvento = lblNombreEvento.getText();    
            controller.cargarDatosBoleto(idEvento,idPersona, nombreEvento); // Pasar el ID del evento
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

    protected RadioButton crearBotonSilla(String nombreSilla, int idSilla, Set<Integer> sillasOcupadas) {
        RadioButton silla = new RadioButton(nombreSilla);
        silla.setToggleGroup(toggleGroupSillas); // Asignar el RadioButton al grupo

        boolean esOcupada = sillasOcupadas.contains(idSilla);

        if (esOcupada) {
            silla.setStyle("-fx-text-fill: red;"); // Cambiar el color del texto a rojo si está ocupada
            silla.setDisable(true); // Deshabilitar si está ocupada
        }

        silla.setOnAction(e -> calcularTotalPagar());
        return silla;
    }

    protected void cerrarVentana() {
        Stage stage = (Stage) txtNombrePersona.getScene().getWindow();
        stage.close();
    }

    protected double calcularTotalPagar() {
        double totalPagar = costoRegular; // Iniciar con el costo regular del evento

        // Verificar si se seleccionó una silla
        if (toggleGroupSillas.getSelectedToggle() != null) {
            RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
            // Verificar si es una silla VIP
            boolean esVip = gridSillasVip.getChildren().stream()
                    .anyMatch(node -> node instanceof RadioButton && node.equals(sillaSeleccionada));

            if (esVip) {
                totalPagar += (costoRegular * porcentajeExtra / 100); // Sumar el porcentaje adicional
            }
        }

        lblTotalPagar.setText("Total a Pagar: $" + totalPagar); // Actualizar el total en la etiqueta

        return totalPagar;
    }

    protected abstract boolean idPersonaAsignadaEvento();
}
