package modelo;

import java.util.Date;

public class Pelicula {
    //variables de entrada
    private int idPelicula;
    private String nombre;
    private String sinopsis;
    private Genero idGenero;
    private byte[] foto;
    private Date fechaEstreno;
    private Double precio;
    private String trailerUrl;

    // Constructor vacío
    public Pelicula() {}

    // Constructor con parámetros
    public Pelicula(int idPelicula, String nombre, String sinopsis, Genero idGenero, byte[] foto,
            Date fechaEstreno, Double precio, String trailerUrl) {
        this.idPelicula = idPelicula;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.idGenero = idGenero;
        this.foto = foto;
        this.fechaEstreno = fechaEstreno;
        this.precio = precio;
        this.trailerUrl = trailerUrl;
        
        //última capa de validación en capa modelo
        validar();
    }

    // Validación estructural (antes de guardar)
    private void validar() {
        if (idPelicula < 0) {
            throw new IllegalArgumentException("El ID de la película debe ser positivo.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la película no puede estar vacío.");
        }

        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre de la película no debe superar los 100 caracteres.");
        }

        if (sinopsis == null || sinopsis.trim().isEmpty()) {
            throw new IllegalArgumentException("La sinopsis no puede estar vacía.");
        }

        if (idGenero == null) {
            throw new IllegalArgumentException("La película debe tener un género asignado.");
        }

        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }

        if (fechaEstreno == null) {
            throw new IllegalArgumentException("La fecha de estreno no puede ser nula.");
        }

        if (trailerUrl != null && !trailerUrl.trim().isEmpty()) {
            if (!trailerUrl.matches("^(https?://)?(www\\.)?([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(/\\S*)?$")) {
                throw new IllegalArgumentException("La URL del tráiler no tiene un formato válido.");
            }
        }
    }

    // Getters y Setters
    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        if (idPelicula < 0) {
            throw new IllegalArgumentException("El ID de la película debe ser positivo.");
        }
        this.idPelicula = idPelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no debe superar los 100 caracteres.");
        }
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        if (sinopsis == null || sinopsis.trim().isEmpty()) {
            throw new IllegalArgumentException("La sinopsis no puede estar vacía.");
        }
        this.sinopsis = sinopsis;
    }

    public Genero getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Genero idGenero) {
        if (idGenero == null) {
            throw new IllegalArgumentException("Debe asignarse un género a la película.");
        }
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
        if (fechaEstreno == null) {
            throw new IllegalArgumentException("La fecha de estreno no puede ser nula.");
        }
        this.fechaEstreno = fechaEstreno;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        this.precio = precio;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        if (trailerUrl != null && !trailerUrl.trim().isEmpty()) {
            if (!trailerUrl.matches("^(https?://)?(www\\.)?([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(/\\S*)?$")) {
                throw new IllegalArgumentException("La URL del tráiler no tiene un formato válido.");
            }
        }
        this.trailerUrl = trailerUrl;
    }
}
