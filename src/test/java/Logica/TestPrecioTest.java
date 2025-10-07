package Logica;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para TestPrecio
 */
public class TestPrecioTest {
    
    public TestPrecioTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of esValido method, of class TestPrecio.
     */
    @Test
    public void testEsValidoCorrecto() {
        System.out.println("esValido - caso válido");
        String precio = "199.99";
        boolean expResult = true;
        boolean result = TestPrecio.esValido(precio);
        assertEquals(expResult, result);
        if(result != expResult)
            fail("El test falló: el precio debería ser válido.");
    }

    @Test
    public void testEsValidoIncorrecto() {
        System.out.println("esValido - caso inválido");
        String precio = "1000000"; // mayor a 999999.99
        boolean expResult = false;
        boolean result = TestPrecio.esValido(precio);
        assertEquals(expResult, result);
        if(result != expResult)
            fail("El test falló: el precio debería ser inválido.");
    }
}
