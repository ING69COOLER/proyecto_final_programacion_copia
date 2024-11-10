package co.edu.uniquindio.poo.Boleto;

import java.io.IOException;
import java.util.Optional;

import co.edu.uniquindio.poo.editar_Evento.EditarEventoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BoletoController extends BaseBoleto {

    @FXML
    void eliminar(ActionEvent event) {
        Optional<String> result = mostrarDialogoEliminarEvento();
        result.ifPresent(claveEmpresarial -> {

            // Validar clave empresarial
            if (!validarClaveEmpresarial(claveEmpresarial)) {
                System.out.println("Clave empresarial incorrecta.");
                return;
            }

            eliminarBoletoYPersona(idEvento, idPersona);
            
            try {
                
                regresar();
                

            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

   

    @FXML
    void imprimir(ActionEvent event) {
        System.out.println("Imprimir");
    }

    @FXML
    void initialize() {
    }

}
