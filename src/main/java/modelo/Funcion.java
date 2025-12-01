package modelo;

import java.sql.Timestamp;

public class Funcion {
    //variables de entrada
    private int idFuncion;
    private Pelicula pelicula;
    private Sala sala;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private EstadoFuncion estadoFuncion;
    private int asientosDisponibles;
    private int activa;

    // Constructor vacío
    public Funcion() {}

    //Constructor con parámetros
    public Funcion(int idFuncion, Pelicula pelicula, Sala sala, Timestamp fechaInicio, Timestamp fechaFin, EstadoFuncion estadoFuncion, int asientosDisponibles, int activa) {
        this.idFuncion = idFuncion;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoFuncion = estadoFuncion;
        this.asientosDisponibles = asientosDisponibles;
        this.activa = activa;
        
        //última capa de validación en capa modelo
        validar();
    }
    
    //Validación estructural (antes de guardar)
    private void validar() {
        if (idFuncion < 0) {
            throw new IllegalArgumentException("El ID de la función debe ser positivo.");
        }

        if (pelicula == null) {
            throw new IllegalArgumentException("La función debe tener una película asociada.");
        }

        if (sala == null) {
            throw new IllegalArgumentException("La función debe tener una sala asignada.");
        }

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
        }

        if (fechaFin.before(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        if (estadoFuncion == null) {
            throw new IllegalArgumentException("Debe especificarse un estado de función.");
        }

        if (asientosDisponibles < 0) {
            throw new IllegalArgumentException("La cantidad de asientos disponibles no puede ser negativa.");
        }
    }
    
    // Getters y setters
    public int getIdFuncion() { 
        return idFuncion; 
    }
    
    public void setIdFuncion(int idFuncion) { 
        this.idFuncion = idFuncion; 
    }

    public Pelicula getPelicula() { 
        return pelicula; 
    }
    
    public void setPelicula(Pelicula pelicula) { 
        this.pelicula = pelicula; 
    }

    public Sala getSala() {
        return sala; 
    }
    
    public void setSala(Sala sala) {
        this.sala = sala; 
    }

    public Timestamp getFechaInicio() { 
        return fechaInicio; 
    }
    
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio; 
    }

    public Timestamp getFechaFin() {
        return fechaFin; 
    }
    
    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin; 
    }

    public EstadoFuncion getEstadoFuncion() {
        return estadoFuncion; 
    }
    
    public void setEstadoFuncion(EstadoFuncion estadoFuncion) {
        this.estadoFuncion = estadoFuncion; 
    }

    public int getAsientosDisponibles() {
        return asientosDisponibles; 
    }
    
    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles; 
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }
}
