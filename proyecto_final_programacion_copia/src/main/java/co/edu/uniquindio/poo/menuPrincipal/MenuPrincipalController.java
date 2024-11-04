package co.edu.uniquindio.poo.menuPrincipal;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Proxy.ProxyEvento;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;

public class MenuPrincipalController extends AMenuPrincipal  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_agregar_avento;

    @FXML
    private Button btn_balance;

    @FXML
    private Button primaryButton;

    @FXML
    private VBox vboxEventos;

    @FXML
    private TextArea labelSillasLibres;

    @FXML
    private Button btn_eliminar_evento;

    // Método para abrir la ventana de balance
    @FXML
    void abrir_ventana_balance() throws IOException {
        App.setRoot("balance");
    }

    // Método para cambiar a la ventana de inicio de sesión
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("inicio_Sesion");
    }

    // Método para cambiar a la ventana de crear evento
    @FXML
    private void ventana_agregar_evento() throws IOException {
        App.setRoot("crear_evento");
    }

    // Método para cargar los eventos desde la base de datos
    @FXML
    private void cargarEventos() {
        ArrayList<Evento> eventos = ProxyEvento.getInstance().getEventos();
       for (Evento evento : eventos) {
                String nombreEvento = evento.getNombre();
                int idEvento = evento.getId();

                // Crear un botón para cada evento
                Button eventoBtn = new Button(nombreEvento);
                eventoBtn.setStyle(
                        "-fx-background-color: linear-gradient(yellow, #FF8C00);" + // Degradado de blanco a naranja oscuro
                        "-fx-text-fill: black;" +    // Color de texto blanco
                        "-fx-font-family: 'Forte';" + // Fuente "Forte"
                        "-fx-background-radius: 20;" + // Bordes redondeados
                        "-fx-padding: 10px 20px;" +  // Espaciado interno (alto y ancho)
                        "-fx-font-size: 14px;" +     // Tamaño de fuente
                        "-fx-border-radius: 20;"     // Bordes redondeados en el borde exterior
                    );

                
                eventoBtn.setPrefWidth(200);
                eventoBtn.setOnAction(e -> editarEvento(idEvento));

                // Agregar el botón al VBox
                vboxEventos.getChildren().add(eventoBtn);
            }
    }

    
    @FXML
    void eliminar_evento(ActionEvent event) {
        Optional<Pair<String, String>> result = mostrarDialogoEliminarEvento();
        result.ifPresent(datos -> {
            String nombreEvento = datos.getKey();
            String claveEmpresarial = datos.getValue();

            // Validar clave empresarial
            if (!validarClaveEmpresarial(claveEmpresarial)) {
                System.out.println("Clave empresarial incorrecta.");
                return;
            }

            // Eliminar el evento y personas relacionadas
            if (eliminarPersonasYEvento(nombreEvento)) {
                limpiarVistaEventos();
                cargarEventos();
            }
        });
    }
    // Este método se llama al inicializar el controlador
    @FXML
    void initialize() {
    
        cargarEventos(); // Cargar los eventos al abrir la ventana
        iniciarActualizacionAutomatica();
    }
    public void limpiarVistaEventos() {
        // Elimina todos los nodos hijos del VBox que contiene los botones de eventos
        vboxEventos.getChildren().clear();
    }

    public void actualizarResumenSillas() {
        String url = "jdbc:sqlite:proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";


        try (Connection con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement();
                ResultSet eventos = stmt.executeQuery("SELECT * FROM Evento")) {

            StringBuilder resumen = new StringBuilder();

            while (eventos.next()) {
                String nombreEvento = eventos.getString("Nombre");
                int idEvento = eventos.getInt("Id");

                // Obtener el resumen para el evento actual
                String eventoResumen = super.obtenerResumenEvento(con, idEvento, nombreEvento);
                resumen.append(eventoResumen);
            }

            // Actualiza el label con la información
            labelSillasLibres.setText(resumen.toString());

        } catch (Exception e) {
            System.out.println("Error al contar las sillas: " + e);
        }
    }

      //Se va
      public void iniciarActualizacionAutomatica() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> actualizarResumenSillas()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
