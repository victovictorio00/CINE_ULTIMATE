package modelo;

import java.sql.Timestamp;

public class Funcion {
    private int idFuncion;
    private Pelicula pelicula;
    private Sala sala;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private EstadoFuncion estadoFuncion;
    private int asientosDisponibles;

    // Getters y setters
    public int getIdFuncion() { return idFuncion; }
    public void setIdFuncion(int idFuncion) { this.idFuncion = idFuncion; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    public Timestamp getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

    public Timestamp getFechaFin() { return fechaFin; }
    public void setFechaFin(Timestamp fechaFin) { this.fechaFin = fechaFin; }

    public EstadoFuncion getEstadoFuncion() { return estadoFuncion; }
    public void setEstadoFuncion(EstadoFuncion estadoFuncion) { this.estadoFuncion = estadoFuncion; }

    public int getAsientosDisponibles() { return asientosDisponibles; }
    public void setAsientosDisponibles(int asientosDisponibles) { this.asientosDisponibles = asientosDisponibles; }
}
