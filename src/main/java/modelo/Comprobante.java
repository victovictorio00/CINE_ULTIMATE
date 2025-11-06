package modelo;
import java.util.Date;

public class Comprobante {
    //variables de entrada
    private int id_comprobante;
    private Venta id_venta;
    private String tipo_comprobante;
    private Date fecha_emision;

    //constructor vacío
    public Comprobante() {}

    //constructor con parámetros
    public Comprobante(int id_comprobante, Venta id_venta, String tipo_comprobante, Date fecha_emision) {
        this.id_comprobante = id_comprobante;
        this.id_venta = id_venta;
        this.tipo_comprobante = tipo_comprobante;
        this.fecha_emision = fecha_emision;
        
        //última capa de validación en capa modelo
        validar();
    }
    
    // Validación estructural (antes de guardar)
    public void validar (){
        if (id_venta == null) {
            throw new IllegalArgumentException("La venta asociada no puede ser nula.");
        }

        if (tipo_comprobante == null || tipo_comprobante.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de comprobante no puede estar vacío.");
        }

        if (!tipo_comprobante.equalsIgnoreCase("boleta") &&
            !tipo_comprobante.equalsIgnoreCase("factura")) {
            throw new IllegalArgumentException("Tipo de comprobante inválido. Solo se permite 'boleta' o 'factura'.");
        }

        if (fecha_emision == null) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser nula.");
        }

        if (fecha_emision.after(new Date())) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser futura.");
        }
    }
    

    // getters y setters
    public int getId_comprobante() {
        return id_comprobante;
    }

    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    public Venta getId_venta() {
        return id_venta;
    }

    public void setId_venta(Venta id_venta) {
        if (id_venta == null) throw new IllegalArgumentException("Venta no puede ser nula.");
        this.id_venta = id_venta; 
    }

    public String getTipo_comprobante() {
        return tipo_comprobante;
    }

    public void setTipo_comprobante(String tipo_comprobante) {
        if (tipo_comprobante == null || tipo_comprobante.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de comprobante no puede estar vacío.");
        }
        this.tipo_comprobante = tipo_comprobante;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        if (fecha_emision == null || fecha_emision.after(new Date())) {
            throw new IllegalArgumentException("Fecha de emisión inválida.");
        }
        this.fecha_emision = fecha_emision;
    }
}
