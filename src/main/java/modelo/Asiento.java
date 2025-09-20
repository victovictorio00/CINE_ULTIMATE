package modelo;

public class Asiento {
    private int id_asiento;
    private Sala id_sala;
    private String codigo;
    private EstadoAsiento id_estado_asiento;

    public Asiento() {}

    public int getId_asiento() {
        return id_asiento;
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public Sala getId_sala() {
        return id_sala;
    }

    public void setId_sala(Sala id_sala) {
        this.id_sala = id_sala;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public EstadoAsiento getId_estado_asiento() {
        return id_estado_asiento;
    }

    public void setId_estado_asiento(EstadoAsiento id_estado_asiento) {
        this.id_estado_asiento = id_estado_asiento;
    }
}
