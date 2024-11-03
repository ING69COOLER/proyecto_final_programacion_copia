package co.edu.uniquindio.poo.Objetos;

public class Evento {
    int id;
    String nombre;
    int costo;
    String tipo;
    double porcentajeExtra;

    public Evento(int id, String nombre, int costo, String tipo, double porcentajeExtra) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
        this.tipo = tipo;
        this.porcentajeExtra = porcentajeExtra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPorcentajeExtra() {
        return porcentajeExtra;
    }

    public void setPorcentajeExtra(double porcentajeExtra) {
        this.porcentajeExtra = porcentajeExtra;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
