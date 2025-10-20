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
    
    
    // Funciones de Reglas del negocio papa

    public boolean esCodigoValido() {
        // El codigo seria fila por columna tipo "A12"
        //return codigo != null && codigo.matches("^[A-Z]+[0-9]+");
        //return codigo != null && codigo.matches("^[A-Z][0-9]{2}$");
        return codigo != null && codigo.matches("^[A-ZÑ][0-9]{2}$");
    }
    // Verificar si esta disponible
    public boolean estaDisponible() {
        return id_estado_asiento != null &&
               "Disponible".equalsIgnoreCase(id_estado_asiento.getNombre());
    }

    //Ocupar asiento
    public void ocupar() {
        if (estaDisponible()) {
            id_estado_asiento.setNombre("Ocupado");
        } else {
            throw new IllegalStateException("El asiento no está disponible para ocupar.");
        }
    }

    // Liberar una asiento si esta ocupado
    public void liberar() {
        if (!estaDisponible()) {
            id_estado_asiento.setNombre("Disponible");
        } else{
            throw new IllegalStateException("El asiento ya estaba disponible");
        }
    }

    // 5. Comparar si dos asientos son el mismo por su código
    public boolean mismoAsiento(Asiento otro) {
        return otro != null &&
               this.codigo != null &&
               this.codigo.equals(otro.getCodigo());
    }
    public Asiento(int id_asiento, boolean disponible) {
        this.id_asiento = id_asiento;
        this.id_estado_asiento = new EstadoAsiento();
        if (disponible) {
            this.id_estado_asiento.setNombre("Disponible");
        } else {
            this.id_estado_asiento.setNombre("Ocupado");
        }
    }
}
