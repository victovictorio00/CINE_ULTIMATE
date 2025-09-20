package modelo;

public class EstadoAsiento {
    private int idEstadoAsiento;
    private String nombre;

    // Constructor
    public EstadoAsiento() {}

    public EstadoAsiento(int idEstadoAsiento, String nombre) {
        this.idEstadoAsiento = idEstadoAsiento;
        this.nombre = nombre;
    }

    public int getIdEstadoAsiento() {
        return idEstadoAsiento;
    }

    public void setIdEstadoAsiento(int idEstadoAsiento) {
        this.idEstadoAsiento = idEstadoAsiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}
