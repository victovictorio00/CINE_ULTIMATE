package modelo;

public class EstadoUsuario {
    //variables de entrada
    private int idEstadoUsuario;
    private String nombre;
    
    //Constructor vacio
    public EstadoUsuario(){}

    // Constructor con parámetros
    public EstadoUsuario(int idEstadoUsuario, String nombre) {
        this.idEstadoUsuario = idEstadoUsuario;
        this.nombre = nombre;
        
        //última capa de validación en capa modelo
        validar();
    }
    
    // Método de validación
    private void validar() {
        if (idEstadoUsuario < 0) {
            throw new IllegalArgumentException("El ID del estado de usuario debe ser positivo.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado de usuario no puede estar vacío.");
        }

        if (nombre.length() > 15) {
            throw new IllegalArgumentException("El nombre del estado de usuario no debe superar los 50 caracteres.");
        }
    }
    
    // getters y setters
    public int getIdEstadoUsuario() {
        return idEstadoUsuario;
    }

    public void setIdEstadoUsuario(int idEstadoUsuario) {
        this.idEstadoUsuario = idEstadoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
