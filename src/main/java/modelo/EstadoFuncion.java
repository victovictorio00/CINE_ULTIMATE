/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Desktop
 */
public class EstadoFuncion {
    private int idEstadoFuncion;
    private String nombre;

    public EstadoFuncion() {}

    public EstadoFuncion(int idEstadoFuncion, String nombre) {
        this.idEstadoFuncion = idEstadoFuncion;
        this.nombre = nombre;
    }

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
