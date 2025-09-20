package modelo;

public class EstadoAsiento {
    private int id_estado_asiento;
    private String descripcion;

    // Constructor
    public EstadoAsiento() {}

    public EstadoAsiento(int id_estado_asiento, String descripcion) {
        this.id_estado_asiento = id_estado_asiento;
        this.descripcion = descripcion;
    }
    

    public int getId_estado_asiento() {
        return id_estado_asiento;
    }

    public void setId_estado_asiento(int id_estado_asiento) {
        this.id_estado_asiento = id_estado_asiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
