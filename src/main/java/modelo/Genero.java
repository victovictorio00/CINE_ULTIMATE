package modelo;

public class Genero {
    //variables de entrada
    private int idGenero;
    private String nombre;
    
    // Constructor vacío
    public Genero(){}

    // Constructor con parámetros
    public Genero(int idGenero, String nombre) {
        this.idGenero = idGenero;
        this.nombre = nombre;
        
        if (nombre != null) {
            validar();
        }
    }

    // Validación estructural (antes de guardar)
    private void validar() {
        if (idGenero < 0) {
            throw new IllegalArgumentException("El ID del género debe ser positivo.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        }
    }
    
    // getters y setters
    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        if (idGenero < 0) {
            throw new IllegalArgumentException("El ID del género debe ser positivo.");
        }
        this.idGenero = idGenero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío");
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener caracteres alfabéticos y espacios");
        }
        this.nombre = nombre;
    }
}