package modelo;

import java.util.Date;

public class Venta {
    private int idVenta;
    private Usuario idUsuarioCliente;
    private Date fecha;
    private double total;
    private String metodoPago;

    // Constructor
    public Venta() {}

    public Venta(int idVenta, Usuario idUsuarioCliente, Date fecha, double total, String metodoPago) {
        this.idVenta = idVenta;
        this.idUsuarioCliente = idUsuarioCliente;
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    
    
}
