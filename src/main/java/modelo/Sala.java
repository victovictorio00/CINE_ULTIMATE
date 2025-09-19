package modelo;

public class Sala {
    
    private int idSala;
    private String nombre;
    private int capacidad;
    
    //constructor vacio
    public Sala() {
    }
    
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
        this.idSala = idSala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}
