package modelo;
public class Empleado {
    //variables de entrada
    private int idEmpleado;
    private String nombre;
    private String direccion;
    private String telefono;
    private String cargo;
    private double salario;

    // Constructor vacío
    public Empleado() {}

    // Constructor con parámetros
    public Empleado(int idEmpleado, String nombre, String direccion, String telefono, String cargo, double salario) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.cargo = cargo;
        this.salario = salario;
        
        //última capa de validación en capa modelo
        validar();
    }

    // Método de validación
    public void validar() {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del empleado no puede estar vacío.");

        if (direccion == null || direccion.trim().isEmpty())
            throw new IllegalArgumentException("La dirección no puede estar vacía.");

        if (telefono == null || !telefono.matches("\\d{7,9}"))
            throw new IllegalArgumentException("El teléfono debe contener entre 7 y 9 dígitos numéricos.");

        if (cargo == null || cargo.trim().isEmpty())
            throw new IllegalArgumentException("El cargo no puede estar vacío.");

        if (salario <= 0)
            throw new IllegalArgumentException("El salario debe ser mayor que cero.");
    }
    
    //Getter and Setter
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

}

