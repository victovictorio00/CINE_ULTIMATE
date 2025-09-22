/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package modelo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class AsientoTest {
    
    public AsientoTest() {
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
     * Test of getId_asiento method, of class Asiento.
     */
    @Test
    public void testGetId_asiento() {
        System.out.println("getId_asiento");
        Asiento instance = new Asiento();
        int expResult = 0;
        int result = instance.getId_asiento();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId_asiento method, of class Asiento.
     */
    @Test
    public void testSetId_asiento() {
        System.out.println("setId_asiento");
        int id_asiento = 0;
        Asiento instance = new Asiento();
        instance.setId_asiento(id_asiento);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId_sala method, of class Asiento.
     */
    @Test
    public void testGetId_sala() {
        System.out.println("getId_sala");
        Asiento instance = new Asiento();
        Sala expResult = null;
        Sala result = instance.getId_sala();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId_sala method, of class Asiento.
     */
    @Test
    public void testSetId_sala() {
        System.out.println("setId_sala");
        Sala id_sala = null;
        Asiento instance = new Asiento();
        instance.setId_sala(id_sala);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCodigo method, of class Asiento.
     */
    @Test
    public void testGetCodigo() {
        System.out.println("getCodigo");
        Asiento instance = new Asiento();
        String expResult = "";
        String result = instance.getCodigo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCodigo method, of class Asiento.
     */
    @Test
    public void testSetCodigo() {
        System.out.println("setCodigo");
        String codigo = "";
        Asiento instance = new Asiento();
        instance.setCodigo(codigo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId_estado_asiento method, of class Asiento.
     */
    @Test
    public void testGetId_estado_asiento() {
        System.out.println("getId_estado_asiento");
        Asiento instance = new Asiento();
        EstadoAsiento expResult = null;
        EstadoAsiento result = instance.getId_estado_asiento();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId_estado_asiento method, of class Asiento.
     */
    @Test
    public void testSetId_estado_asiento() {
        System.out.println("setId_estado_asiento");
        EstadoAsiento id_estado_asiento = null;
        Asiento instance = new Asiento();
        instance.setId_estado_asiento(id_estado_asiento);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of esCodigoValido method, of class Asiento.
     */
    @Test
    public void testEsCodigoValido() {
        System.out.println("esCodigoValido");
        Asiento instance = new Asiento();
        String codigo_testeo = "C05";
        instance.setCodigo(codigo_testeo);
        boolean expResult = true;
        boolean result = instance.esCodigoValido();
        assertEquals(expResult, result);
    }

    /**
     * Test of estaDisponible method, of class Asiento.
     */
    @Test
    public void testEstaDisponible() {
        System.out.println("estaDisponible");
        Asiento instance = new Asiento();
        EstadoAsiento estado = new EstadoAsiento(1,"Disponible");
        instance.setId_estado_asiento(estado);
        
        boolean expResult = true;
        boolean result = instance.estaDisponible();
        assertEquals(expResult, result);
    }

    /**
     * Test of ocupar method, of class Asiento.
     */
    @Test
    public void testOcupar() {
        System.out.println("ocupar");
        EstadoAsiento estado = new EstadoAsiento(1, "Disponible");
        Asiento instance = new Asiento();
        instance.setId_estado_asiento(estado);
        instance.ocupar();
        String expResult = "Ocupado";
        assertEquals(expResult, instance.getId_estado_asiento().getDescripcion());
    }

    /**
     * Test of liberar method, of class Asiento.
     */
    @Test
    public void testLiberar() {
        System.out.println("liberar");
        EstadoAsiento estado = new EstadoAsiento(2, "Ocupado");
        Asiento instance = new Asiento();
        instance.setId_estado_asiento(estado);
        instance.liberar();
        
        String expResult = "Disponible";
        assertEquals(expResult, instance.getId_estado_asiento().getDescripcion());
    }

    /**
     * Test of mismoAsiento method, of class Asiento.
     */
    @Test
    public void testMismoAsiento() {
        System.out.println("mismoAsiento");
        Asiento a1 = new Asiento();
        a1.setCodigo("A10");
        Asiento a2 = new Asiento();
        a2.setCodigo("A10");

        boolean result = a1.mismoAsiento(a2);
        boolean expResult = true;
        assertEquals(expResult,result);
    }
    
}
