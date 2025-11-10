package modelo;

import java.util.Date;
import java.util.List;

/**
 * Clase modelo: Venta
 * ------------------------------------------------------
 * Representa una transacción o compra realizada por un cliente en el sistema CineMax.
 *
 * Cada venta está asociada a:
 *  - Un usuario (cliente).
 *  - Una fecha y hora de registro.
 *  - Un monto total (suma de los detalles).
 *  - Un método de pago.
 *  - Una lista de detalles (boletos o productos de dulcería).
 *
 * Esta clase cumple una función clave dentro del flujo de reservas
 * y compra de entradas en CineMax.
 */
public class Venta {

    // ==========================================================
    // ATRIBUTOS PRINCIPALES
    // ==========================================================

    /** Identificador único de la venta. */
    private int idVenta;

    /** Usuario que realiza la compra (cliente autenticado). */
    private Usuario idUsuarioCliente;

    /** Fecha y hora en que se generó la venta. */
    private Date fecha;

    /** Monto total de la venta. */
    private double total;

    /** Método de pago utilizado (por ejemplo: "Tarjeta", "Efectivo"). */
    private String metodoPago;

    /** Lista de los detalles (entradas, productos, combos, etc.). */
    private List<DetalleVenta> detalles;


    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    /** Constructor vacío (usado por frameworks o DAOs). */
    public Venta() {}

    /**
     * Constructor con parámetros.
     *
     * @param idVenta Identificador de la venta.
     * @param idUsuarioCliente Usuario asociado a la venta.
     * @param fecha Fecha y hora de la venta.
     * @param total Monto total de la venta.
     * @param metodoPago Método de pago utilizado.
     */
    public Venta(int idVenta, Usuario idUsuarioCliente, Date fecha, double total, String metodoPago) {
        this.idVenta = idVenta;
        this.idUsuarioCliente = idUsuarioCliente;
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
    }


    // ==========================================================
    // GETTERS Y SETTERS
    // ==========================================================

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Usuario getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(Usuario idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    /**
     * Asigna la fecha de la venta validando que no sea nula.
     *
     * @param fecha Fecha a registrar.
     * @throws IllegalArgumentException si la fecha es nula.
     */
    public void setFecha(Date fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    /**
     * Asigna el monto total de la venta.
     *
     * @param total Monto total (debe ser positivo).
     * @throws IllegalArgumentException si el monto es negativo.
     */
    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo.");
        }
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Define el método de pago validando que no sea vacío.
     *
     * @param metodoPago Texto del método de pago.
     * @throws IllegalArgumentException si está vacío o nulo.
     */
    public void setMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío.");
        }
        this.metodoPago = metodoPago;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    /**
     * Asigna la lista de detalles de la venta (boletos o productos).
     *
     * @param detalles Lista de objetos {@link DetalleVenta}.
     */
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }


    // ==========================================================
    // MÉTODOS AUXILIARES (Opcionales)
    // ==========================================================

    /**
     * Calcula el total de la venta en base a los detalles asociados.
     * Si no hay detalles, el total será 0.
     */
    public void recalcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            this.total = 0;
        } else {
            this.total = detalles.stream()
                    .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                    .sum();
        }
    }

    /**
     * Retorna una descripción resumida de la venta.
     *
     * @return Cadena con el ID y el monto total.
     */
    @Override
    public String toString() {
        return "Venta #" + idVenta + " | Total: S/" + total + " | Método: " + metodoPago;
    }
}
