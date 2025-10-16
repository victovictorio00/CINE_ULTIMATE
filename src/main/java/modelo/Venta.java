package modelo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Venta {
    private int idVenta;
    private Usuario idUsuarioCliente;
    private Date fecha;
    private double total;
    private String metodoPago;

    // Constructor
    public Venta() {}

    public Venta(int idVenta, Usuario idUsuarioCliente, Date fecha, double total, String metodoPago) {
        this.idVenta = idVenta;
        this.idUsuarioCliente = idUsuarioCliente;
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
    }

    
     public void setTotal(double total) {
           if (total < 0) {
               throw new IllegalArgumentException("El total no puede ser negativo");
           }
           this.total = total; // ya sin caracteres raros
       }
     
     
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Usuario getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(Usuario idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(String fechaStr) {
        if (fechaStr == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        // Patrón específico
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm");
        sdf.setLenient(false); // no acepta fechas "raras" como 32/13/25

        try {
            Date fecha = sdf.parse(fechaStr);
            this.fecha = fecha;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Debe ser dd/MM/yy - HH:mm");
        }
    }

    public double getTotal() {
        return total;
    }

   

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    
    
}
