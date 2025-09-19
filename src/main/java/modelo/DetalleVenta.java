package modelo;

public class DetalleVenta {
    
    private int idDetalleVenta;
    //private Venta venta;
    private Producto producto;
    //private Funcion funcion;
    //private Asiento asiento;
    private int cantidad;
    private int tipoItem;
    private double precioUnitario;

    //constructor vacío
    public DetalleVenta() {
    }

    //constructor con parámetros
    /*public DetalleVenta(int idDetalleVenta, Venta venta, Producto producto, Funcion funcion, Asiento asiento, int cantidad, int tipoItem, double precioUnitario) {
        this.idDetalleVenta = idDetalleVenta;
        this.venta = venta;
        this.producto = producto;
        this.funcion = funcion;
        this.asiento = asiento;
        this.cantidad = cantidad;
        this.tipoItem = tipoItem;
        this.precioUnitario = precioUnitario;
    }*/

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }
/*
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
*/
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
/*
    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }
*/
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(int tipoItem) {
        this.tipoItem = tipoItem;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
