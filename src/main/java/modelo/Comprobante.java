package modelo;

import java.util.Date;

public class Comprobante {
    private int id_comprobante;
    private Ventas id_venta;
    private String tipo_comprobante;
    private Date fecha_emision;

    public Comprobante() {}

    public int getId_comprobante() {
        return id_comprobante;
    }

    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    public Ventas getId_venta() {
        return id_venta;
    }

    public void setId_venta(Ventas id_venta) {
        this.id_venta = id_venta;
    }

    public String getTipo_comprobante() {
        return tipo_comprobante;
    }

    public void setTipo_comprobante(String tipo_comprobante) {
        this.tipo_comprobante = tipo_comprobante;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }
}
