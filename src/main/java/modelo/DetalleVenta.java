package modelo;
public class DetalleVenta {
    //variables de entrada
    private int idDetalleVenta;
    private Venta venta;
    private Producto producto;
    private Funcion funcion;
    private Asiento asiento;
    private int cantidad;
    private int tipoItem;
    private double precioUnitario;
    
    //constructor vacío
    public DetalleVenta() {
    }

    //constructor con parámetros
    public DetalleVenta(int idDetalleVenta, Venta venta, Producto producto, Funcion funcion, Asiento asiento, int cantidad, int tipoItem, double precioUnitario) {
        this.idDetalleVenta = idDetalleVenta;
        this.venta = venta;
        this.producto = producto;
        this.funcion = funcion;
        this.asiento = asiento;
        this.cantidad = cantidad;
        this.tipoItem = tipoItem;
        this.precioUnitario = precioUnitario;
        
        //última capa de validación en capa modelo
        validar();
    }   

    // --- MÉTODO DE VALIDACIÓN ---
    public void validar() {
        StringBuilder errores = new StringBuilder();

        // idDetalleVenta no debe ser negativo
        if (idDetalleVenta < 0) {
            errores.append("El ID del detalle de venta no puede ser negativo.\n");
        }

        // Validar la venta asociada
        if (venta == null || venta.getIdVenta() <= 0) {
            errores.append("Debe asociarse a una venta válida.\n");
        }

        // Validar el tipo de item (producto o entrada)
        if (tipoItem != 1 && tipoItem != 2) {
            errores.append("El tipo de ítem debe ser 1 (producto) o 2 (entrada).\n");
        }

        // Validar producto si corresponde
        if (tipoItem == 1) {
            if (producto == null || producto.getIdProducto() <= 0) {
                errores.append("Debe especificarse un producto válido cuando el tipo de ítem es producto.\n");
            }
        }

        // Validar función y asiento si corresponde
        if (tipoItem == 2) {
            if (funcion == null || funcion.getIdFuncion() <= 0) {
                errores.append("Debe especificarse una función válida cuando el tipo de ítem es entrada.\n");
            }
            if (asiento == null || asiento.getId_asiento() <= 0) {
                errores.append("Debe especificarse un asiento válido cuando el tipo de ítem es entrada.\n");
            }
        }

        // Cantidad mayor a 0
        if (cantidad <= 0) {
            errores.append("La cantidad debe ser mayor a 0.\n");
        }

        // Precio unitario mayor a 0
        if (precioUnitario <= 0) {
            errores.append("El precio unitario debe ser mayor a 0.\n");
        }

        // Si se detectan errores, se lanza excepción
        if (errores.length() > 0) {
            throw new IllegalArgumentException("Error en los datos del DetalleVenta:\n" + errores.toString());
        }
    }
    
    //getters y setters
    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
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
