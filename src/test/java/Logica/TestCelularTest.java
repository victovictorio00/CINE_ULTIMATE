package Logica;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCelularTest {
    
    public TestCelularTest() {
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

    @org.junit.Test
    public void testNumeroValido() {
        System.out.println("numeroValido");
        String numero = "953455a1";
        boolean expResult = false;
        boolean result = TestCelular.numeroValido(numero);
        assertEquals(expResult, result);
        if(expResult != result)
            fail("The test case is a prototype.");
    }
    
}
