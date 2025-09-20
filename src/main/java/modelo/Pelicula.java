/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.util.Date;

public class Pelicula {

    private int idPelicula;
    private String nombre;
    private String sinopsis;
    private Genero idGenero;
    private byte[] foto;
    private Date fechaEstreno;
    private Double precio;

    // Constructor vacío
    public Pelicula() {
    }

    // Constructor con parámetros

    public Pelicula(int idPelicula, String nombre, String sinopsis, Genero idGenero, byte[] foto, Date fechaEstreno, Double precio) {
        this.idPelicula = idPelicula;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.idGenero = idGenero;
        this.foto = foto;
        this.fechaEstreno = fechaEstreno;
        this.precio = precio;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Genero getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Genero idGenero) {
        this.idGenero = idGenero;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Date getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(Date fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
}
