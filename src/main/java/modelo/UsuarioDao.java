package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class UsuarioDao implements DaoCrud<Usuario> {

    @Override
    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuarios";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setDni(rs.getString("dni"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setNumeroIntentos(rs.getInt("numero_intentos"));

                // Rol
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                usuario.setIdRol(rol);

                // Estado
                EstadoUsuario estado = new EstadoUsuario();
                estado.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
                usuario.setIdEstadoUsuario(estado);

                lista.add(usuario);
            }
        }
        return lista;
    }

    @Override
    public void insertar(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuarios (id_rol, id_estado_usuario, nombre_completo, dni, username, password, telefono, email, direccion, numero_intentos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, usuario.getIdRol().getIdRol());
            pst.setInt(2, usuario.getIdEstadoUsuario().getIdEstadoUsuario());
            pst.setString(3, usuario.getNombreCompleto());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());
            pst.setString(6, usuario.getPassword());
            pst.setString(7, usuario.getTelefono());
            pst.setString(8, usuario.getEmail());
            pst.setString(9, usuario.getDireccion());
            pst.setInt(10, usuario.getNumeroIntentos());

            pst.executeUpdate();
        }
    }

    @Override
    public Usuario leer(int id) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreCompleto(rs.getString("nombre_completo"));
                    usuario.setDni(rs.getString("dni"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setDireccion(rs.getString("direccion"));
                    usuario.setNumeroIntentos(rs.getInt("numero_intentos"));

                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    usuario.setIdRol(rol);

                    EstadoUsuario estado = new EstadoUsuario();
                    estado.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
                    usuario.setIdEstadoUsuario(estado);

                    return usuario;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Usuario usuario) throws SQLException {
        String query = "UPDATE usuarios SET id_rol = ?, id_estado_usuario = ?, nombre_completo = ?, dni = ?, username = ?, password = ?, telefono = ?, email = ?, direccion = ?, numero_intentos = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, usuario.getIdRol().getIdRol());
            pst.setInt(2, usuario.getIdEstadoUsuario().getIdEstadoUsuario());
            pst.setString(3, usuario.getNombreCompleto());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());
            pst.setString(6, usuario.getPassword());
            pst.setString(7, usuario.getTelefono());
            pst.setString(8, usuario.getEmail());
            pst.setString(9, usuario.getDireccion());
            pst.setInt(10, usuario.getNumeroIntentos());
            pst.setInt(11, usuario.getIdUsuario());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // Validación de usuario
    public boolean validateUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }
}
