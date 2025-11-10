package modelo;

/**
 * Clase: AsientoFuncion
 * 
 * Representa la relación entre un Asiento y una Función en el cine.
 * 
 * Cada registro indica el estado actual de un asiento específico dentro de
 * una función (por ejemplo: disponible, ocupado, bloqueado, reservado).
 * 
 * En otras palabras, es una tabla intermedia entre:
 * - Asiento  → el asiento físico dentro de una sala.
 * - Funcion  → la proyección de una película en una fecha y hora específicas.
 * 
 * Ejemplo:
 *   idAsientoFuncion = 35
 *   asiento = A5 (Sala 1)
 *   funcion = “Spider-Man: 20:00 - 22:00”
 *   estadoAsiento = “Ocupado”
 */
public class AsientoFuncion {

    // ============================================================
    // ATRIBUTOS / VARIABLES
    // ============================================================

    /** ID único que identifica la relación asiento-función en la base de datos. */
    private int idAsientoFuncion;

    /** Asiento físico asociado (número o código dentro de la sala). */
    private Asiento asiento;

    /** Función (película + horario) donde participa el asiento. */
    private Funcion funcion;

    /** Estado del asiento dentro de esta función (disponible, ocupado, bloqueado, etc.). */
    private EstadoAsiento estadoAsiento;


    // ============================================================
    // CONSTRUCTORES
    // ============================================================

    /** Constructor vacío: permite crear el objeto sin inicializar campos. */
    public AsientoFuncion() {}

    /**
     * Constructor con parámetros: inicializa todos los campos.
     * 
     * @param idAsientoFuncion identificador único
     * @param asiento objeto Asiento asociado
     * @param funcion objeto Funcion asociada
     * @param estadoAsiento estado actual del asiento dentro de la función
     */
    public AsientoFuncion(int idAsientoFuncion, Asiento asiento, Funcion funcion, EstadoAsiento estadoAsiento) {
        this.idAsientoFuncion = idAsientoFuncion;
        this.asiento = asiento;
        this.funcion = funcion;
        this.estadoAsiento = estadoAsiento;
    }

    // ============================================================
    // GETTERS Y SETTERS
    // ============================================================

    /** @return el ID único de la relación asiento-función */
    public int getIdAsientoFuncion() {
        return idAsientoFuncion;
    }

    /** @param idAsientoFuncion asigna el ID único */
    public void setIdAsientoFuncion(int idAsientoFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
    }

    /** @return el asiento físico asociado */
    public Asiento getAsiento() {
        return asiento;
    }

    /** @param asiento establece el asiento físico asociado */
    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    /** @return la función (película + horario) asociada */
    public Funcion getFuncion() {
        return funcion;
    }

    /** @param funcion asigna la función correspondiente */
    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    /** @return el estado del asiento dentro de la función */
    public EstadoAsiento getEstadoAsiento() {
        return estadoAsiento;
    }

    /** @param estadoAsiento establece el estado del asiento */
    public void setEstadoAsiento(EstadoAsiento estadoAsiento) {
        this.estadoAsiento = estadoAsiento;
    }
}
