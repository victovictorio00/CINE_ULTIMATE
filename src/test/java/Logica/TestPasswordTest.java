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

/**
 *
 * @author Desktop
 */
public class TestPasswordTest {
    
    public TestPasswordTest() {
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
     * Test of esValida method, of class TestPassword.
     */
    @Test
    public void testEsValida() {
        System.out.println("esValida");
        String Password = "Contra_Valida";//iniciamos con mayuscula
        boolean expResult = true;
        boolean result = TestPassword.esValida(Password);
        if(result != expResult)
            fail("");
    }
    
}
