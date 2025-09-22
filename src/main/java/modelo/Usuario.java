package modelo;

/**
 *
 * @author Desktop
 */
public class Usuario {
    private int idUsuario;
    private Rol idRol;
    private EstadoUsuario idEstadoUsuario;
    private String nombreCompleto;
    private String dni;
    private String username;
    private String password;
    private String telefono;
    private String email;
    private String direccion;
    private int numeroIntentos;
    
    // Constructor
    public Usuario() {}

    public Usuario(int idUsuario, Rol idRol, EstadoUsuario idEstadoUsuario, String nombreCompleto, String dni, String username, String password, String telefono, String email, String direccion, int numeroIntentos) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.idEstadoUsuario = idEstadoUsuario;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.username = username;
        this.password = password;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.numeroIntentos = numeroIntentos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol idRol) {
        this.idRol = idRol;
    }

    public EstadoUsuario getIdEstadoUsuario() {
        return idEstadoUsuario;
    }

    public void setIdEstadoUsuario(EstadoUsuario idEstadoUsuario) {
        this.idEstadoUsuario = idEstadoUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNumeroIntentos() {
        return numeroIntentos;
    }

    public void setNumeroIntentos(int numeroIntentos) {
        this.numeroIntentos = numeroIntentos;
    }
    
}
