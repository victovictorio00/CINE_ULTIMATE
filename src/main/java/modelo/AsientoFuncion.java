package modelo;

public class AsientoFuncion {

    // variables de entrada
    private int idAsientoFuncion;
    private Asiento asiento;
    private Funcion funcion;
    private EstadoAsiento estadoAsiento;

    // Constructor vacío
    public AsientoFuncion() {}

    // Constructor con parámetros
    public AsientoFuncion(int idAsientoFuncion, Asiento asiento, Funcion funcion, EstadoAsiento estadoAsiento) {
        this.idAsientoFuncion = idAsientoFuncion;
        this.asiento = asiento;
        this.funcion = funcion;
        this.estadoAsiento = estadoAsiento;
    }

    // Getters y setters
    public int getIdAsientoFuncion() {
        return idAsientoFuncion;
    }

    public void setIdAsientoFuncion(int idAsientoFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public EstadoAsiento getEstadoAsiento() {
        return estadoAsiento;
    }

    public void setEstadoAsiento(EstadoAsiento estadoAsiento) {
        this.estadoAsiento = estadoAsiento;
    }
}