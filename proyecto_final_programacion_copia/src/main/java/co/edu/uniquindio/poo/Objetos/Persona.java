package co.edu.uniquindio.poo.Objetos;

public class Persona {
    private int idPersona;
    private String nombrePersona;
    private int idEvento;
    private int idSilla;
    private String tipoSilla;
    private double totalPagar;

    public Persona(int idPersona, String nombrePersona, int idEvento, int idSilla, String tipoSilla, double totalPagar) {
        this.idPersona = idPersona;
        this.nombrePersona = nombrePersona;
        this.idEvento = idEvento;
        this.idSilla = idSilla;
        this.tipoSilla = tipoSilla;
        this.totalPagar = totalPagar;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getIdSilla() {
        return idSilla;
    }

    public void setIdSilla(int idSilla) {
        this.idSilla = idSilla;
    }

    public String getTipoSilla() {
        return tipoSilla;
    }

    public void setTipoSilla(String tipoSilla) {
        this.tipoSilla = tipoSilla;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }                                                                                                           
                                                                                                                                              
    
}
