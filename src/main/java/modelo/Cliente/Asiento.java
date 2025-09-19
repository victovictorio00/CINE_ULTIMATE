/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.Cliente;

/**
 *
 * @author Proyecto
 */

public class Asiento {
    private int numero;
    private boolean disponible;

    public Asiento(int numero, boolean disponible) {
        this.numero = numero;
        this.disponible = disponible;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
