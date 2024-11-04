package co.edu.uniquindio.poo.Balance.Excel;

import com.opencsv.CSVWriter;

import co.edu.uniquindio.poo.Balance.IExportar;
import co.edu.uniquindio.poo.Objetos.Evento;
import co.edu.uniquindio.poo.Objetos.Persona;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ToExcel implements IExportar {

    /**
     * @param listaEventos
     * @param listaPersonas
     */
    @Override 
    public void exportar(ArrayList<Evento> listaEventos, ArrayList<Persona> listaPersonas) {
        // Nombre del archivo CSV
        String nombreArchivo = "proyecto_final_programacion_copia/src/main/java/co/edu/uniquindio/poo/Balance/Excel/Eventos_Personas.csv";
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(nombreArchivo))) {
            
            // Encabezado de las columnas en el archivo CSV
            String[] encabezado = {"Evento", "Nombre", "ID Persona", "Tipo Silla", "ID Silla", "Total a Pagar"};
            writer.writeNext(encabezado);

            // Escribir datos de cada persona asociada a un evento
            for (Evento evento : listaEventos) {
                for (Persona persona : listaPersonas) {
                    if (persona.getIdEvento() == evento.getId()) {
                        String[] datosPersona = {
                            evento.getNombre(),                           // Nombre del evento
                            persona.getNombrePersona(),                   // Nombre de la persona
                            String.valueOf(persona.getIdPersona()),       // ID Persona
                            persona.getTipoSilla(),                       // Tipo de Silla
                            String.valueOf(persona.getIdSilla()),         // ID Silla
                            String.valueOf(persona.getTotalPagar())       // Total a Pagar
                        };
                        writer.writeNext(datosPersona);
                    }
                }
            }
            
            System.out.println("Archivo CSV creado exitosamente: " + nombreArchivo);
            
        } catch (IOException e) {
            System.out.println("Error al crear el archivo CSV: " + e.getMessage());
        }
    }
}
