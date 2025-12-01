package modelo;

public class Asiento {

    //variables de entrada
    private int id_asiento;
    private Sala id_sala;
    private String codigo;
    private EstadoAsiento id_estado_asiento;

    //constructor vacío
    public Asiento() {
    }

    //constructor con parámetros
    public Asiento(int id_asiento, Sala id_sala, String codigo, EstadoAsiento id_estado_asiento) {
        this.id_asiento = id_asiento;
        this.id_sala = id_sala;
        this.codigo = codigo;
        this.id_estado_asiento = id_estado_asiento;
    }

    //getters and setters
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

    // Funciones de Reglas del negocio papa
    public void setEstado(EstadoAsiento nuevoEstado) {
        this.id_estado_asiento = nuevoEstado;
    }

    // Método que valida código - "A12"
    public boolean esCodigoValido() {
        return codigo != null && codigo.matches("^[A-ZÑ][0-9]{2}$");
    }

    // Verificar si esta disponible
    public boolean estaDisponible() {
        return id_estado_asiento != null && id_estado_asiento.getIdEstadoAsiento() == 1;
    }

    // Comparar si dos asientos son el mismo por su código
    public boolean mismoAsiento(Asiento otro) {
        return otro != null && this.codigo != null && this.codigo.equals(otro.getCodigo());
    }

    // Métodos útiles para el JSP
    public String getFila() {
        if (codigo != null && codigo.length() > 0) {
            return codigo.substring(0, 1); // Extrae "A" de "A1"
        }
        return "";
    }

    public int getNumero() {
        if (codigo != null && codigo.length() > 1) {
            try {
                return Integer.parseInt(codigo.substring(1)); // Extrae "1" de "A1"
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
