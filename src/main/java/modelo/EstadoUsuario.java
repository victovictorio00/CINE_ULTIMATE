
package modelo;

public class EstadoUsuario {
    private int idEstadoUsuario;
    private String nombre;
    
    public EstadoUsuario(){}

    public EstadoUsuario(int idEstadoUsuario, String nombre) {
        this.idEstadoUsuario = idEstadoUsuario;
        this.nombre = nombre;
    }

    public int getIdEstadoUsuario() {
        return idEstadoUsuario;
    }

    public void setIdEstadoUsuario(int idEstadoUsuario) {
        this.idEstadoUsuario = idEstadoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
