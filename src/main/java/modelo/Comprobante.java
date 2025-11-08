package modelo;
import java.sql.Timestamp;
import java.util.Date;

public class Comprobante {
    //variables de entrada
    private int id_comprobante;
    private Venta venta;
    private String tipoComprobante;
    private Timestamp fechaEmision;

    //constructor vacío
    public Comprobante() {}

    //constructor con parámetros
    public Comprobante(int id_comprobante, Venta id_venta, String tipo_comprobante, Timestamp fecha_emision) {
        this.id_comprobante = id_comprobante;
        this.venta = id_venta;
        this.tipoComprobante = tipo_comprobante;
        this.fechaEmision = fecha_emision;
    }

    // getters y setters
    public int getId_comprobante() {
        return id_comprobante;
    }

    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta id_venta) {
        if (id_venta == null) throw new IllegalArgumentException("Venta no puede ser nula.");
        this.venta = id_venta; 
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipo_comprobante) {
        if (tipo_comprobante == null || tipo_comprobante.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de comprobante no puede estar vacío.");
        }
        this.tipoComprobante = tipo_comprobante;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Timestamp fecha_emision) {
        if (fecha_emision == null || fecha_emision.after(new Date())) {
            throw new IllegalArgumentException("Fecha de emisión inválida.");
        }
        this.fechaEmision = fecha_emision;
    }
}
