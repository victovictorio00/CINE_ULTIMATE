package modelo;

import java.util.Date;

public class Ventas {
    private int id_venta;
    private Usuarios id_usuario_cliente;
    private Date fecha;
    private double total;
    private String metodo_pago;

    // Constructor
    public Ventas() {}

    public Ventas(int id_venta, Usuarios id_usuario_cliente, Date fecha, double total, String metodo_pago) {
        this.id_venta = id_venta;
        this.id_usuario_cliente = id_usuario_cliente;
        this.fecha = fecha;
        this.total = total;
        this.metodo_pago = metodo_pago;
    }
    
    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public Usuarios getId_usuario_cliente() {
        return id_usuario_cliente;
    }

    public void setId_usuario_cliente(Usuarios id_usuario_cliente) {
        this.id_usuario_cliente = id_usuario_cliente;
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

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }
}
