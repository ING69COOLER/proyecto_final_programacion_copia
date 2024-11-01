package co.edu.uniquindio.poo.editar_Evento;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseEditarEventoController {

    @FXML
    protected Label lblNombreEvento, lblCostoEvento, lblTotalPagar;

    @FXML
    protected GridPane gridSillas, gridSillasVip;

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

    protected void cargarSillasEnGrid(Connection con, String query, GridPane grid, Set<Integer> sillasOcupadas) throws SQLException {
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

    protected RadioButton crearBotonSilla(String nombreSilla, int idSilla, Set<Integer> sillasOcupadas) {
        RadioButton silla = new RadioButton(nombreSilla);
        silla.setToggleGroup(toggleGroupSillas); // Asignar el RadioButton al grupo

        boolean esOcupada = sillasOcupadas.contains(idSilla);
        System.out.println("Verificando silla: ID=" + idSilla + ", Nombre=" + nombreSilla + ", Ocupada=" + esOcupada); // Depuración

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

    
    protected abstract boolean idPersonaAsignadaEvento(Connection con) throws SQLException;
}
