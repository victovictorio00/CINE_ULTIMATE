package modelo;
import java.util.Date;

public class Venta {
    // variables de entrada
    private int idVenta;
    private Usuario idUsuarioCliente;
    private Date fecha;
    private double total;
    private String metodoPago;

    // Constructor vacío
    public Venta() {
    }

    // Constructor con parámetros
    public Venta(int idVenta, Usuario idUsuarioCliente, Date fecha, double total, String metodoPago) {
        this.idVenta = idVenta;
        this.idUsuarioCliente = idUsuarioCliente;
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
        
        //última capa de validación en capa modelo
        validar();
    }

    // Validación estructural (antes de guardar)
    private void validar() {
        if (idUsuarioCliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío");
        }
    }
    
    // getters y setters
    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        this.total = total; // ya sin caracteres raros
    }

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

    public void setFecha(Date fechaStr) {
        if (fechaStr == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío");
        }
        this.metodoPago = metodoPago;
    }
}
