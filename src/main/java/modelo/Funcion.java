/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author Desktop
 */
public class Funcion {
    private int idFuncion;
    private Pelicula idPelicula;
    private Sala idSala;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int idEstadoFuncion;
    private int asientosDisponibles;
    
    //constructor Vacio
    public Funcion(){}

    public Funcion(int idFuncion, Pelicula idPelicula, Sala idSala, LocalDate fechaInicio, LocalDate fechaFin, int idEstadoFuncion, int asientosDisponibles) {
        this.idFuncion = idFuncion;
        this.idPelicula = idPelicula;
        this.idSala = idSala;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idEstadoFuncion = idEstadoFuncion;
        this.asientosDisponibles = asientosDisponibles;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public Pelicula getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Pelicula idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Sala getIdSala() {
        return idSala;
    }

    public void setIdSala(Sala idSala) {
        this.idSala = idSala;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdEstadoFuncion() {
        return idEstadoFuncion;
    }

    public void setIdEstadoFuncion(int idEstadoFuncion) {
        this.idEstadoFuncion = idEstadoFuncion;
    }

    public int getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    
    
    
}
