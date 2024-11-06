package co.edu.uniquindio.poo.Balance.Excel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import co.edu.uniquindio.poo.Balance.IExportar;
import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Objetos.Persona;

public class AdapterExcel implements IExportar {

    private static AdapterExcel adapterExcel;

    private AdapterExcel() {
    }

    public static AdapterExcel getInstance() {
        if (adapterExcel == null) {
            adapterExcel = new AdapterExcel();
        }
        return adapterExcel;
    }

    @Override
    public void exportar(ArrayList<Evento> listaEventos, ArrayList<Persona> listaPersonas) {
        // Configuración de Gson para convertir a JSON
        Gson gson = new Gson();

        // Creación de un mapa para almacenar ambas listas
        Map<String, Object> data = new HashMap<>();
        data.put("listaEventos", listaEventos);
        data.put("listaPersonas", listaPersonas);

        // Serializar las listas y guardarlas en un archivo JSON
        try (FileWriter file = new FileWriter("proyecto_final_programacion_copia/src/main/java/co/edu/uniquindio/poo/Balance/Excel/datos.json")) {
            gson.toJson(data, file);
            System.out.println("Exportación completada: datos.json creado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\Users\\USUARIO\\Documents\\Manuel\\SEMESTRE_3\\Programacion_2\\proyecto_final_programacion_copia\\proyecto_final_programacion_copia\\proyecto_final_programacion_copia\\src\\main\\java\\co\\edu\\uniquindio\\poo\\Balance\\Excel\\Main.py");

            processBuilder.inheritIO(); // Para que Python pueda mostrar su salida en la consola de Java
            Process process = processBuilder.start();
            process.waitFor(); // Espera a que el proceso termine antes de continuar

            System.out.println("El script de Python se ejecutó correctamente.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
