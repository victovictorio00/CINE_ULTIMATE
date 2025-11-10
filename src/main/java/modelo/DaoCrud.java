package modelo;

import java.util.List;
import java.sql.SQLException;

/**
 * Interfaz genérica: DaoCrud<T>
 * ------------------------------------------------------
 * Define las operaciones básicas de acceso a datos (CRUD)
 * que deben implementar todas las clases DAO del sistema CineMax.
 *
 * Esta interfaz permite aplicar el patrón de diseño DAO (Data Access Object)
 * de forma genérica y reutilizable para cualquier entidad del modelo.
 *
 * @param <T> Tipo genérico que representa la entidad del modelo (por ejemplo: Usuario, Pelicula, Comprobante, etc.)
 */
public interface DaoCrud<T> {

    /**
     * Recupera una lista completa de todos los registros de la entidad T
     * desde la base de datos.
     *
     * @return Lista de objetos de tipo T.
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL.
     */
    List<T> listar() throws SQLException;

    /**
     * Inserta un nuevo registro de tipo T en la base de datos.
     *
     * @param obj Objeto de tipo T que contiene los datos a guardar.
     * @throws SQLException si ocurre un error al ejecutar la inserción SQL.
     */
    void insertar(T obj) throws SQLException;

    /**
     * Busca y devuelve un registro de tipo T según su identificador único.
     *
     * @param id Identificador del registro a buscar.
     * @return Objeto de tipo T si existe, o null si no se encuentra.
     * @throws SQLException si ocurre un error durante la búsqueda SQL.
     */
    T leer(int id) throws SQLException;

    /**
     * Actualiza un registro existente de tipo T en la base de datos.
     *
     * @param obj Objeto de tipo T con los nuevos datos actualizados.
     * @throws SQLException si ocurre un error durante la actualización SQL.
     */
    void editar(T obj) throws SQLException;

    /**
     * Elimina un registro de la base de datos según su identificador.
     *
     * @param id Identificador del registro a eliminar.
     * @throws SQLException si ocurre un error durante la eliminación SQL.
     */
    void eliminar(int id) throws SQLException;
}
