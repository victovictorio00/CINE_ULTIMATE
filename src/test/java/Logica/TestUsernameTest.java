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
public class TestUsernameTest {
    
    public TestUsernameTest() {
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
     * Test of esValida method, of class TestUsername.
     */
    @Test
    public void testEsValida() {
        System.out.println("esValida");
        String Username = "a2345saas6";
        boolean expResult = true;
        boolean result = TestUsername.esValida(Username);
        assertEquals(expResult, result);
        if(result != expResult)
        fail("The test case is a prototype.");
    }
    
}
