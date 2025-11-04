
package modelo.Cliente;


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
