package modelo;

public class EstadoFuncion {
    //variables de entrada
    private int idEstadoFuncion;
    private String nombre;

    // Constructor vacío
    public EstadoFuncion() {}

    // Constructor con parámetros
    public EstadoFuncion(int idEstadoFuncion, String nombre) {
        this.idEstadoFuncion = idEstadoFuncion;
        this.nombre = nombre;
        
        //última capa de validación en capa modelo
        validar();
    }

    // Método de validación
    private void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado de función no puede estar vacío.");
        }

        if (nombre.length() > 50) {
            throw new IllegalArgumentException("El nombre del estado de función no debe superar los 50 caracteres.");
        }

        if (idEstadoFuncion < 0) {
            throw new IllegalArgumentException("El ID del estado de función debe ser positivo.");
        }
    }
    
    // getters y setters
    public int getIdEstadoFuncion() {
        return idEstadoFuncion;
    }

    public void setIdEstadoFuncion(int idEstadoFuncion) {
        this.idEstadoFuncion = idEstadoFuncion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
