package modelo;

import java.util.List;
import java.sql.SQLException;

public interface DaoCrud<T> {
    List<T> listar() throws SQLException;
    void insertar(T obj) throws SQLException;
    T leer(int id) throws SQLException;
    void editar(T obj) throws SQLException;
    void eliminar(int id) throws SQLException;
}