package modelo;

public class Empleado {
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

