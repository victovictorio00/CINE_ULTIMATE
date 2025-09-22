/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Logica;

import modelo.Genero;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ferna
 */
public class GeneroTest {

 public void testNombreValido() {
        Genero genero = new Genero();
        genero.setNombre("Acción");
        assertEquals("Acción", genero.getNombre());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNombreNuloLanzaExcepcion() {
        Genero genero = new Genero();
        genero.setNombre(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNombreVacioLanzaExcepcion() {
        Genero genero = new Genero();
        genero.setNombre("   "); // solo espacios
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNombreConNumerosLanzaExcepcion() {
        Genero genero = new Genero();
        genero.setNombre("Accion123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNombreConSimbolosLanzaExcepcion() {
        Genero genero = new Genero();
        genero.setNombre("Drama!");
    }
    
}
