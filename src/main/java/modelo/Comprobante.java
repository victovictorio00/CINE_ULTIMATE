package modelo;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase: Comprobante
 * ------------------------------------------------------
 * Representa un comprobante de pago generado a partir de una venta
 * dentro del sistema CineMax.
 * 
 * Cada comprobante pertenece a una venta específica y contiene información
 * sobre:
 *  - El tipo de comprobante (boleta, factura, ticket)
 *  - La fecha y hora de emisión
 *
 * Este modelo es esencial para la gestión de comprobantes electrónicos
 * o impresos dentro del módulo de ventas.
 */
public class Comprobante {

    // ==========================================================
    // ATRIBUTOS / VARIABLES
    // ==========================================================

    /** Identificador único del comprobante (clave primaria en la BD). */
    private int id_comprobante;

    /** Venta asociada a la cual pertenece este comprobante. */
    private Venta venta;

    /** Tipo de comprobante emitido (por ejemplo: "Boleta", "Factura", "Ticket"). */
    private String tipoComprobante;

    /** Fecha y hora exacta de emisión del comprobante. */
    private Timestamp fechaEmision;


    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    /** 
     * Constructor vacío: permite crear el objeto sin inicializar campos.
     * 
     * Útil cuando se usa frameworks ORM o mapeo manual desde la BD.
     */
    public Comprobante() {}

    /**
     * Constructor con parámetros: crea un objeto comprobante con todos sus datos.
     * 
     * @param id_comprobante Identificador único
     * @param id_venta Venta asociada
     * @param tipo_comprobante Tipo de comprobante (boleta, factura, etc.)
     * @param fecha_emision Fecha y hora de emisión del comprobante
     */
    public Comprobante(int id_comprobante, Venta id_venta, String tipo_comprobante, Timestamp fecha_emision) {
        this.id_comprobante = id_comprobante;
        this.venta = id_venta;
        this.tipoComprobante = tipo_comprobante;
        this.fechaEmision = fecha_emision;
    }


    // ==========================================================
    // MÉTODOS GETTERS Y SETTERS
    // ==========================================================

    /** @return el identificador del comprobante */
    public int getId_comprobante() {
        return id_comprobante;
    }

    /** @param id_comprobante asigna el identificador del comprobante */
    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    /** @return la venta asociada a este comprobante */
    public Venta getVenta() {
        return venta;
    }

    /**
     * Asigna la venta correspondiente a este comprobante.
     * 
     * @param id_venta Objeto Venta relacionado
     * @throws IllegalArgumentException si la venta es nula
     */
    public void setVenta(Venta id_venta) {
        if (id_venta == null) 
            throw new IllegalArgumentException("Venta no puede ser nula.");
        this.venta = id_venta; 
    }

    /** @return el tipo de comprobante (Boleta, Factura, etc.) */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Asigna el tipo de comprobante emitido.
     * 
     * @param tipo_comprobante texto con el tipo (ej. "Boleta", "Factura")
     * @throws IllegalArgumentException si el tipo es nulo o vacío
     */
    public void setTipoComprobante(String tipo_comprobante) {
        if (tipo_comprobante == null || tipo_comprobante.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de comprobante no puede estar vacío.");
        }
        this.tipoComprobante = tipo_comprobante;
    }

    /** @return la fecha y hora de emisión del comprobante */
    public Date getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Asigna la fecha y hora de emisión del comprobante.
     * 
     * @param fecha_emision Timestamp de emisión
     * @throws IllegalArgumentException si la fecha es nula o está en el futuro
     */
    public void setFechaEmision(Timestamp fecha_emision) {
        if (fecha_emision == null || fecha_emision.after(new Date())) {
            throw new IllegalArgumentException("Fecha de emisión inválida.");
        }
        this.fechaEmision = fecha_emision;
    }
}
