package modelo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FilaReservaDTO {

    private String pelicula;
    private String sala;
    private Timestamp fechaHora;
    private List<String> asientos = new ArrayList<>();
    private double totalEntradas = 0;
    private double totalProductos = 0;
    private int idVenta;
    private int cantidadProductos = 0;

    public String getRangoAsientos() {
        return asientos.isEmpty() ? "N/A"
                : asientos.size() == 1 ? asientos.get(0)
                : "[" + String.join(", ", asientos) + "]";
    }

    // getters / setters
    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public List<String> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<String> asientos) {
        this.asientos = asientos;
    }

    public double getTotalEntradas() {
        return totalEntradas;
    }

    public void setTotalEntradas(double totalEntradas) {
        this.totalEntradas = totalEntradas;
    }

    public double getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(double totalProductos) {
        this.totalProductos = totalProductos;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(int c) {
        this.cantidadProductos = c;
    }
}
