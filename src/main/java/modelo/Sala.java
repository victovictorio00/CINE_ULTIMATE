package modelo;

public class Sala {
    // variables de entrada
    private int idSala;
    private String nombre;
    private int capacidad;
    
    //constructor vacio
    public Sala() {}
    
    //constructor con parámetros
    public Sala(int idSala, String nombre, int capacidad) {
        this.idSala = idSala;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    //getters and setters
    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        if (idSala < 0)
            throw new IllegalArgumentException("El ID de la sala no puede ser negativo.");
        this.idSala = idSala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
            if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de la sala no puede estar vacío.");
        if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$"))
            throw new IllegalArgumentException("El nombre de la sala solo puede contener letras, números y espacios.");
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        if (capacidad <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        this.capacidad = capacidad;
    }
}
