package modelo;

public class EstadoAsiento {

    //variables de entrada
    private int idEstadoAsiento;
    private String nombre;
    private String Descripcion;

    // Constructor vacío
    public EstadoAsiento() {
    }

    // Constructor con parámetros
    public EstadoAsiento(int idEstadoAsiento, String nombre, String Descripcion) {
        this.idEstadoAsiento = idEstadoAsiento;
        this.nombre = nombre;
        this.Descripcion = Descripcion;

        //última capa de validación en capa modelo
        validar();
    }

    // Método de validación
    private void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado del asiento no puede estar vacío.");
        }

        if (Descripcion == null || Descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del estado del asiento no puede estar vacía.");
        }

        if (nombre.length() > 15) {
            throw new IllegalArgumentException("El nombre no debe superar los 50 caracteres.");
        }

        if (Descripcion.length() > 50) {
            throw new IllegalArgumentException("La descripción no debe superar los 200 caracteres.");
        }
    }

    // getters y setters
    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
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
