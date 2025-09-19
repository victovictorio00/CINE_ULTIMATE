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
public class Funciones {
    private int id_funcion;
    //private peliculas id_pelicula;
    //private salas id_sala;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private int id_estado_funcion;
    private int asientos_disponibles;
    
    //constructor Vacio
    public Funciones(){}
/*
    public Funciones(int id_funcion, peliculas id_pelicula, salas id_sala, LocalDate fecha_inicio, LocalDate fecha_fin, int id_estado_funcion, int asientos_disponibles) {
        this.id_funcion = id_funcion;
        this.id_pelicula = id_pelicula;
        this.id_sala = id_sala;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.id_estado_funcion = id_estado_funcion;
        this.asientos_disponibles = asientos_disponibles;
    }
*/
    public int getId_funcion() {
        return id_funcion;
    }

    public void setId_funcion(int id_funcion) {
        this.id_funcion = id_funcion;
    }
/*
    public peliculas getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(peliculas id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public salas getId_sala() {
        return id_sala;
    }

    public void setId_sala(salas id_sala) {
        this.id_sala = id_sala;
    }
*/
    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getId_estado_funcion() {
        return id_estado_funcion;
    }

    public void setId_estado_funcion(int id_estado_funcion) {
        this.id_estado_funcion = id_estado_funcion;
    }

    public int getAsientos_disponibles() {
        return asientos_disponibles;
    }

    public void setAsientos_disponibles(int asientos_disponibles) {
        this.asientos_disponibles = asientos_disponibles;
    }
    
    
}
