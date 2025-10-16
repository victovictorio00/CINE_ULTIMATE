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
     
     
     //FECHA
     @Test
    public void testFechaValida() {
        Venta venta = new Venta();
        venta.setFecha("10/12/25 - 14:50"); // válido
        assertNotNull(venta.getFecha());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFechaFormatoInvalidoSinHora() {
        Venta venta = new Venta();
        venta.setFecha("10/12/25"); // falta la hora
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFechaFormatoInvalidoSeparadorIncorrecto() {
        Venta venta = new Venta();
        venta.setFecha("10-12-25 - 14:50"); // separador de fecha inválido
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFechaNula() {
        Venta venta = new Venta();
        venta.setFecha(null); // nulo no permitido
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFechaInvalidaDiaMes() {
        Venta venta = new Venta();
        venta.setFecha("32/15/25 - 14:50"); // día y mes no válidos
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFechaInvalidaHora() {
        Venta venta = new Venta();
        venta.setFecha("10/12/25 - 25:99*"); // hora inválida
    }
     
     
     
     
     
     

}