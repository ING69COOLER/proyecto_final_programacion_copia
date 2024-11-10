package co.edu.uniquindio.poo.Objetos;

public class Boleto {
    private int id;
    private String nombre;
    private String nombreEvento;
    private int idPersona;
    private int idEvento;

    public Boleto(int id, String nombre, String nombreEvento, int idPersona, int idEvento) {
        this.id = id;
        this.nombre = nombre;
        this.nombreEvento = nombreEvento;
        this.idPersona = idPersona;
        this.idEvento = idEvento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    @Override
    public String toString() {
        return "Boleto{" + "id=" + id + ", nombre=" + nombre + ", nombreEvento=" + nombreEvento + ", idPersona=" + idPersona + ", idEvento=" + idEvento + '}';
    }
}
