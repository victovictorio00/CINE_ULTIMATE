/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
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

    @Test
    public void testNumeroValido() {
        System.out.println("numeroValido");
        String numero = "3455941";
        boolean expResult = true;
        boolean result = TestCelular.numeroValido(numero);
        if(expResult != result)
            fail("The test case is a prototype.");
    }
}
