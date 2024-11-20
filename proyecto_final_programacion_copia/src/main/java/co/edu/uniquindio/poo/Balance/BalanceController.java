package co.edu.uniquindio.poo.Balance;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.Utils;
import co.edu.uniquindio.poo.Balance.DateBalance.Balance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class BalanceController implements Utils {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BarChart<String, Number> balanceChart;

    @FXML
    private ScrollPane VBoxDate;

    @FXML
    private VBox VBoxGrafico;

    @FXML
    private Button btnExportar;

    @FXML
    private Button btnaRegresar;

    @FXML
    void Regresar() throws IOException {
        App.setRoot("menu_principal");
    }
   
    @FXML
    void exportar(ActionEvent event) {
        mostrarAlertaExito("Exportando", "Exportando datos a archivo...");
        Balance.getInstance().exportar();
        
    }

    // mostrar balance
    @FXML
    private void mostrarBalance(ArrayList<String> balance) {
        VBox vboxContent = new VBox(); // Crear un nuevo VBox para añadir etiquetas

        for (String b : balance) {
            Label label = new Label(b);
            vboxContent.getChildren().add(label); // Añadir cada Label al VBox
        }

        VBoxDate.setContent(vboxContent); // Establecer el VBox como contenido del ScrollPane

    }

    public void actualizarGrafico(ArrayList<String> balance) {
    balanceChart.getData().clear(); // Limpiar datos previos

    for (String evento : balance) {
        // Suponiendo que cada evento está en el formato "Evento: Nombre\nSillas Normales: valor\nSillas VIP: valor\nTotal: valor"
        String[] partes = evento.split("\n");
        String nombreEvento = partes[0].split(": ")[1].trim();
        double totalBalance = Double.parseDouble(partes[3].split(": ")[1].trim());

        // Crear una serie de datos para el evento
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(nombreEvento);
        serie.getData().add(new XYChart.Data<>(nombreEvento, totalBalance));

        // Agregar la serie al gráfico
        balanceChart.getData().add(serie);
    }
}


    @FXML
    void initialize() {
        Balance.getInstance().recargarListas();
        ArrayList<String> balance = Balance.getInstance().obtenerBalance();
        mostrarBalance(balance);
        actualizarGrafico(balance);

    }

}
