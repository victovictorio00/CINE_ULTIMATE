/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


/**
 *
 * @author Desktop
 */
public class Usuarios {
    private int id_usuario;
    private Rol id_rol;
    private EstadoUsuario id_estado_usuario;
    private String nombre_completo;
    private String dni;
    private String username;
    private String password;
    private String telefono;
    private String email;
    private String direccion;
    private int numero_intentos;
    
    // Constructor
    public Usuarios() {}

    public Usuarios(int id_usuario, Rol id_rol, EstadoUsuario id_estado_usuario, String nombre_completo, String dni, String username, String password, String telefono, String email, String direccion, int numero_intentos) {
        this.id_usuario = id_usuario;
        this.id_rol = id_rol;
        this.id_estado_usuario = id_estado_usuario;
        this.nombre_completo = nombre_completo;
        this.dni = dni;
        this.username = username;
        this.password = password;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.numero_intentos = numero_intentos;
    }
    
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Rol getId_rol() {
        return id_rol;
    }

    public void setId_rol(Rol id_rol) {
        this.id_rol = id_rol;
    }
    
    public EstadoUsuario getId_estado_usuario() {
        return id_estado_usuario;
    }

    public void setId_estado_usuario(EstadoUsuario id_estado_usuario) {
        this.id_estado_usuario = id_estado_usuario;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNumero_intentos() {
        return numero_intentos;
    }

    public void setNumero_intentos(int numero_intentos) {
        this.numero_intentos = numero_intentos;
    }
    
}
