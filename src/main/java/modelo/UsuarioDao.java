/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class UsuarioDao implements DaoCrud<Usuarios> {

    @Override
    public List<Usuarios> listar() throws SQLException {
        List<Usuarios> lista = new ArrayList<>();
        String query = "SELECT * FROM usuarios";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Usuarios usuario = new Usuarios();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre_completo(rs.getString("nombre_completo"));
                usuario.setDni(rs.getString("dni"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setNumero_intentos(rs.getInt("numero_intentos"));
                
                //ROL
                //Rol rol = new Rol();
                //rol.setId_rol(rs.getInt("id_rol"));
                //usuario.setId_rol(rol);
                //Estado usuarios
                EstadoUsuarios estado = new EstadoUsuarios();
                estado.setId_estado_usuario(rs.getInt("id_estado_usuario"));
                usuario.setId_estado_usuario(estado);
                
                lista.add(usuario);    
            }
        }
        return lista;
    }

    @Override
    public void insertar(Usuarios usuario) throws SQLException {
        String query = "INSERT INTO usuarios (id_rol, id_estado_usuario, nombre_completo, dni, username, password, telefono, email, direccion, numero_intentos) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            //pst.setInt(1, usuario.getId_rol().getId_rol());
            pst.setInt(2, usuario.getId_estado_usuario().getId_estado_usuario());
            pst.setString(3, usuario.getNombre_completo());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());
            pst.setString(6, usuario.getPassword());
            pst.setString(7, usuario.getTelefono());
            pst.setString(8, usuario.getEmail());
            pst.setString(9, usuario.getDireccion());
            pst.setInt(10, usuario.getNumero_intentos());
            pst.executeUpdate();
        }
    }

    @Override
    public Usuarios leer(int id) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Usuarios usuario = new Usuarios();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombre_completo(rs.getString("nombre_completo"));
                    usuario.setDni(rs.getString("dni"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setDireccion(rs.getString("direccion"));
                    usuario.setNumero_intentos(rs.getInt("numero_intentos"));

                    // Rol
                    //Rol rol = new Rol();
                    //rol.setId_rol(rs.getInt("id_rol"));
                    //usuario.setId_rol(rol);

                    // Estado usuario
                    EstadoUsuarios estado = new EstadoUsuarios();
                    estado.setId_estado_usuario(rs.getInt("id_estado_usuario"));
                    usuario.setId_estado_usuario(estado);

                    return usuario;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Usuarios usuario) throws SQLException {
        String query = "UPDATE usuarios SET id_rol = ?, id_estado_usuario = ?, nombre_completo = ?, dni = ?, username = ?, password = ?, telefono = ?, email = ?, direccion = ?, numero_intentos = ? "
                     + "WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            //pst.setInt(1, usuario.getId_rol().getId_rol());
            pst.setInt(2, usuario.getId_estado_usuario().getId_estado_usuario());
            pst.setString(3, usuario.getNombre_completo());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());
            pst.setString(6, usuario.getPassword());
            pst.setString(7, usuario.getTelefono());
            pst.setString(8, usuario.getEmail());
            pst.setString(9, usuario.getDireccion());
            pst.setInt(10, usuario.getNumero_intentos());
            pst.setInt(11, usuario.getId_usuario());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    
   
    // Validación de usuario por username y password
    public boolean validateUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, username);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Si encuentra al menos un usuario, validación OK
            }
        }
    }

    // Obtener Rol por username
    /*public Rol getRolByUsername(String username) throws SQLException {
        String query = "SELECT id_rol FROM usuarios WHERE username = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol();
                    rol.setId_rol(rs.getInt("id_rol"));
                    return rol;
                }
            }
        }
        return null; // Si no encuentra el usuario, devuelve null
    }
    */
}

    

