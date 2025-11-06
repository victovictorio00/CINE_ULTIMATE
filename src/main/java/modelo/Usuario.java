package modelo;

public class Usuario {
    // variables de entrada
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

    // Constructor vacío
    public Usuario() {}

    // Constructor con parámetros
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
        
        //última capa de validación en capa modelo
        validar();
    }
    
    // Validación estructural (antes de guardar)
    public void validar() {
        StringBuilder errores = new StringBuilder();

        if (idUsuario < 0)
            errores.append("El ID de usuario no puede ser negativo.\n");
        if (idRol == null)
            errores.append("El rol del usuario no puede ser nulo.\n");
        if (idEstadoUsuario == null)
            errores.append("El estado del usuario no puede ser nulo.\n");

        if (nombreCompleto == null || nombreCompleto.trim().isEmpty())
            errores.append("El nombre completo no puede estar vacío.\n");
        else if (!nombreCompleto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            errores.append("El nombre completo solo puede contener letras y espacios.\n");

        if (dni == null || !dni.matches("\\d{8}"))
            errores.append("El DNI debe tener exactamente 8 dígitos numéricos.\n");

        if (username == null || username.trim().isEmpty())
            errores.append("El nombre de usuario no puede estar vacío.\n");
        else if (username.length() < 4)
            errores.append("El nombre de usuario debe tener al menos 4 caracteres.\n");

        if (password == null || password.length() < 6)
            errores.append("La contraseña debe tener al menos 6 caracteres.\n");

        if (telefono != null && !telefono.trim().isEmpty() && !telefono.matches("\\d{9}"))
            errores.append("El teléfono debe tener exactamente 9 dígitos.\n");

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            errores.append("El correo electrónico no tiene un formato válido.\n");

        if (direccion == null || direccion.trim().isEmpty())
            errores.append("La dirección no puede estar vacía.\n");

        if (numeroIntentos < 0)
            errores.append("El número de intentos no puede ser negativo.\n");

        if (errores.length() > 0)
            throw new IllegalArgumentException("Errores en Usuario:\n" + errores.toString());
    }
    
    // getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        if (idUsuario < 0)
            throw new IllegalArgumentException("El ID de usuario no puede ser negativo.");
        this.idUsuario = idUsuario;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol idRol) {
        if (idRol == null)
            throw new IllegalArgumentException("El rol del usuario no puede ser nulo.");
        this.idRol = idRol;
    }

    public EstadoUsuario getIdEstadoUsuario() {
        return idEstadoUsuario;
    }

    public void setIdEstadoUsuario(EstadoUsuario idEstadoUsuario) {
        if (idEstadoUsuario == null)
            throw new IllegalArgumentException("El estado del usuario no puede ser nulo.");
        this.idEstadoUsuario = idEstadoUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty())
            throw new IllegalArgumentException("El nombre completo no puede estar vacío.");
        if (!nombreCompleto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            throw new IllegalArgumentException("El nombre completo solo puede contener letras y espacios.");
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}"))
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos.");
        this.dni = dni;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        if (username.length() < 4)
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 4 caracteres.");
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty() && !telefono.matches("\\d{9}"))
            throw new IllegalArgumentException("El teléfono debe tener exactamente 9 dígitos.");
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty())
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        this.direccion = direccion;
    }

    public int getNumeroIntentos() {
        return numeroIntentos;
    }

    public void setNumeroIntentos(int numeroIntentos) {
        if (numeroIntentos < 0)
            throw new IllegalArgumentException("El número de intentos no puede ser negativo.");
        this.numeroIntentos = numeroIntentos;
    }
}
