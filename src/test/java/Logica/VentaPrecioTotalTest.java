/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Logica;

import java.util.Date;
import modelo.Usuario;
import modelo.Venta;
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
public class VentaPrecioTotalTest {
    
       @Test
     public void testTotalValido() {
         Venta instance = new Venta();
         instance.setTotal(100.0); // válido
         assertEquals(100.0, instance.getTotal(), 0.001);
     }

     @Test(expected = IllegalArgumentException.class)
     public void testTotalNegativoLanzaExcepcion() {
         Venta instance = new Venta();
         instance.setTotal(-50.0); // debe lanzar excepción
     }

}