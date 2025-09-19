/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Desktop
 */
public class EstadoUsuarios {
    private int id_estado_usuario;
    private String nombre;

    public EstadoUsuarios(){}

    public EstadoUsuarios(int id_estado_usuario, String nombre) {
        this.id_estado_usuario = id_estado_usuario;
        this.nombre = nombre;
    }

    public int getId_estado_usuario() {
        return id_estado_usuario;
    }

    public void setId_estado_usuario(int id_estado_usuario) {
        this.id_estado_usuario = id_estado_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
