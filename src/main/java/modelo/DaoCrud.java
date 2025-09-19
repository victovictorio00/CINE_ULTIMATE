/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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

//NO TOCAR