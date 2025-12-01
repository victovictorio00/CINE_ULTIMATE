package modelo;

public class Producto {
    // variables de entrada
    private int idProducto;
    private String nombre;
    private String descripcion;
    private byte[] foto;
    private int stock;
    private double precio;

    // Constructor vacío
    public Producto() {}

    // Constructor con todos los atributos
    public Producto(int idProducto, String nombre, String descripcion, byte[] foto, int stock, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.stock = stock;
        this.precio = precio;

        //última capa de validación en capa modelo
        validar();
    }

    //Validación estructural (antes de guardar)
    public void validar() {
        StringBuilder errores = new StringBuilder();

        if (idProducto < 0) {
            errores.append("El ID del producto no puede ser negativo.\n");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            errores.append("El nombre del producto no puede estar vacío.\n");
        } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            errores.append("El nombre solo puede contener letras, números y espacios.\n");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            errores.append("La descripción no puede estar vacía.\n");
        } else if (descripcion.trim().length() < 10) {
            errores.append("La descripción debe tener al menos 10 caracteres.\n");
        }

        if (stock < 0) {
            errores.append("El stock no puede ser negativo.\n");
        }

        if (precio <= 0) {
            errores.append("El precio debe ser mayor que cero.\n");
        }

        if (foto == null || foto.length == 0) {
            errores.append("Debe adjuntar una imagen del producto.\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException("Errores en Producto:\n" + errores.toString());
        }
    }

    // Getters y setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        if (idProducto < 0) {
            throw new IllegalArgumentException("El ID del producto no puede ser negativo.");
        }
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras, números y espacios.");
        }
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (descripcion.trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres.");
        }
        this.descripcion = descripcion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        if (foto == null || foto.length == 0) {
            throw new IllegalArgumentException("Debe adjuntar una imagen del producto.");
        }
        this.foto = foto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        this.precio = precio;
    }
}
