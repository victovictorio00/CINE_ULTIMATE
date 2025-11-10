package modelo;

/**
 * Clase: DetalleVenta
 * ------------------------------------------------------
 * Representa cada elemento que forma parte de una venta realizada
 * en el sistema CineMax.
 *
 * Un DetalleVenta puede corresponder a:
 *  - Un producto de la dulcería (tipoItem = 2)
 *  - Un asiento reservado para una función de película (tipoItem = 1)
 *
 * Cada detalle está asociado a una venta principal (tabla ventas),
 * e incluye información sobre el producto o función, cantidad y precio.
 */
public class DetalleVenta {

    // ==========================================================
    // ATRIBUTOS
    // ==========================================================

    /** Identificador único del detalle (clave primaria). */
    private int idDetalleVenta;

    /** Venta a la que pertenece este detalle. */
    private Venta venta;

    /** Producto asociado (si el detalle es de dulcería). */
    private Producto producto;

    /** Función de película asociada (si el detalle corresponde a una reserva). */
    private Funcion funcion;

    /** Relación con el asiento reservado para la función. */
    private AsientoFuncion idAsientoFuncion;

    /** Cantidad del ítem vendido (puede ser número de productos o asientos). */
    private int cantidad;

    /** Tipo de ítem: 1 = Asiento/Función, 2 = Producto/Dulcería. */
    private int tipoItem;

    /** Precio unitario del ítem vendido. */
    private double precioUnitario;


    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    /** Constructor vacío (requerido para frameworks y mapeo manual). */
    public DetalleVenta() {}

    /**
     * Constructor con parámetros principales.
     *
     * @param idDetalleVenta Identificador del detalle.
     * @param venta Venta asociada.
     * @param producto Producto asociado (si aplica).
     * @param funcion Función asociada (si aplica).
     * @param cantidad Cantidad de ítems vendidos.
     * @param tipoItem Tipo de ítem (1 = asiento, 2 = producto).
     * @param precioUnitario Precio unitario del ítem.
     */
    public DetalleVenta(int idDetalleVenta, Venta venta, Producto producto, Funcion funcion,
                        int cantidad, int tipoItem, double precioUnitario) {
        this.idDetalleVenta = idDetalleVenta;
        this.venta = venta;
        this.producto = producto;
        this.funcion = funcion;
        this.idAsientoFuncion = null;
        this.cantidad = cantidad;
        this.tipoItem = tipoItem;
        this.precioUnitario = precioUnitario;
    }


    // ==========================================================
    // MÉTODOS GETTERS Y SETTERS
    // ==========================================================

    /** @return el identificador del detalle de venta. */
    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    /** @param idDetalleVenta asigna el identificador del detalle. */
    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    /** @return la venta asociada a este detalle. */
    public Venta getVenta() {
        return venta;
    }

    /** @param venta asigna la venta a la que pertenece el detalle. */
    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    /** @return el producto asociado (si aplica). */
    public Producto getProducto() {
        return producto;
    }

    /** @param producto asigna el producto asociado al detalle. */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /** @return la función de película asociada (si aplica). */
    public Funcion getFuncion() {
        return funcion;
    }

    /** @param funcion asigna la función asociada al detalle. */
    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    /** @return el asiento-función vinculado a la reserva. */
    public AsientoFuncion getIdAsientoFuncion() {
        return idAsientoFuncion;
    }

    /** @param idAsientoFuncion asigna el asiento-función relacionado. */
    public void setIdAsientoFuncion(AsientoFuncion idAsientoFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
    }

    /** @return la cantidad de ítems vendidos. */
    public int getCantidad() {
        return cantidad;
    }

    /** @param cantidad establece la cantidad de ítems vendidos. */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /** @return el tipo de ítem (1 = asiento, 2 = producto). */
    public int getTipoItem() {
        return tipoItem;
    }

    /** @param tipoItem asigna el tipo de ítem (1 = asiento, 2 = producto). */
    public void setTipoItem(int tipoItem) {
        this.tipoItem = tipoItem;
    }

    /** @return el precio unitario del ítem. */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /** @param precioUnitario establece el precio unitario del ítem. */
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
