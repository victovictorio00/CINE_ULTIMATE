package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * POJO que representa una reserva temporal en sesión durante el flujo de venta.
 */
public class ReservaTemporal implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idFuncion;
    private int idPelicula;
    private double precioEntrada;
    private Date fecha;
    private int idSala;

    public ReservaTemporal() {
    }

    public ReservaTemporal(int idFuncion, int idPelicula, double precioEntrada, Date fecha, int idSala) {
        this.idFuncion = idFuncion;
        this.idPelicula = idPelicula;
        this.precioEntrada = precioEntrada;
        this.fecha = fecha;
        this.idSala = idSala;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public double getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(double precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    @Override
    public String toString() {
        return "ReservaTemporal{" +
                "idFuncion=" + idFuncion +
                ", idPelicula=" + idPelicula +
                ", precioEntrada=" + precioEntrada +
                ", fecha=" + fecha +
                ", idSala=" + idSala +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservaTemporal that = (ReservaTemporal) o;
        return idFuncion == that.idFuncion &&
                idPelicula == that.idPelicula &&
                Double.compare(that.precioEntrada, precioEntrada) == 0 &&
                idSala == that.idSala &&
                Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFuncion, idPelicula, precioEntrada, fecha, idSala);
    }
}
