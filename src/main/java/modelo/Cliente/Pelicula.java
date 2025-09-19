/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.Cliente;

public class Pelicula {
    private int id;
    private String title;
    private String sinopsis;
    private String imagePath;
    private String genero; // Agregado el campo genero
    private String trailerUrl; // Agregado el campo trailerUrl

    // Constructor
    public Pelicula(int id, String title, String sinopsis, String imagePath, String genero, String trailerUrl) {
        this.id = id;
        this.title = title;
        this.sinopsis = sinopsis;
        this.imagePath = imagePath;
        this.genero = genero;
        this.trailerUrl = trailerUrl;
    }

    // Métodos getter y setter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getGenero() {
        return genero;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }
}
