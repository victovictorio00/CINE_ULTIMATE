/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.Cliente;

import java.util.ArrayList;
import java.util.List;

public class PeliculaDaoCliente {

    // Lista estática que simula las películas disponibles para el cliente
    private static List<Pelicula> peliculas = new ArrayList<>();

    static {
        // Agregamos algunas películas ficticias
peliculas.add(new Pelicula(1, "Lilo y Stitch", "Aventura Familiar | 2h 44m | Una historia sobre una extraterrestre y una niña...", "Cliente/images/bladerunner.jpg", "Ciencia Ficción", "abcd1234"));
peliculas.add(new Pelicula(2, "Django Unchained", "18+ | 2h 45m | Un western épico...", "Cliente/images/django.jpg", "Western", "efgh5678"));
peliculas.add(new Pelicula(3, "Max Payne", "18+ | 1h 58m | Un thriller de acción...", "Cliente/images/maxpayne.jpg", "Acción", "ijkl91011"));
peliculas.add(new Pelicula(4, "Red Coat", "16+ | 2h 30m | Una historia de espionaje...", "Cliente/images/redcoat.jpg", "Espionaje", "mnop121314"));
peliculas.add(new Pelicula(5, "Lilo y Stitch", "Aventura Familiar | 1h 30m | Una historia sobre una extraterrestre y una niña...", "Cliente/images/liloystitch.jpg", "Familiar", "qrst151617"));

        // Agrega más películas según sea necesario
    }

    // Método para obtener todas las películas disponibles
    public List<Pelicula> listar() {
        return peliculas;
    }

    // Método para obtener una película específica por ID
    public Pelicula getPeliculaById(int id) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId() == id) {
                return pelicula;
            }
        }
        return null;  // Si no se encuentra la película
    }
}
