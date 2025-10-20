package modelo.Cliente;

import java.sql.Date;

public class Pelicula {
    private int idPelicula;
    private String nombre;
    private String sinopsis;
    private String genero;
    private String trailerUrl;
    private String foto; // ruta o base64 de la imagen

    public Pelicula() {}

    public Pelicula(int idPelicula, String nombre, String sinopsis, String genero, String trailerUrl, String foto) {
        this.idPelicula = idPelicula;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.trailerUrl = trailerUrl;
        this.foto = foto;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
