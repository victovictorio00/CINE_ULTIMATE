package modelo;

public class Rol {
    // variables de entrada
    private int idRol;
    private String nombre;

    public Rol() {}

    public Rol(int idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
        
        //última capa de validación en capa modelo
        validar();
    }

    // Validación estructural (antes de guardar)
    public void validar() {
        StringBuilder errores = new StringBuilder();

        if (idRol < 0) {
            errores.append("El ID del rol no puede ser negativo.\n");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            errores.append("El nombre del rol no puede estar vacío.\n");
        } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            errores.append("El nombre del rol solo puede contener letras y espacios.\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException("Errores en Rol:\n" + errores.toString());
        }
    }
    
    // getters y setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        if (idRol < 0)
            throw new IllegalArgumentException("El ID del rol no puede ser negativo.");
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío.");
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            throw new IllegalArgumentException("El nombre del rol solo puede contener letras y espacios.");
        this.nombre = nombre;
    }   
}
