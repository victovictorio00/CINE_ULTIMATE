package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDao implements DaoCrud<Usuario> {

    // Columnas explícitas (evita SELECT *)
    private static final String COLS = String.join(",",
            "id_usuario",
            "id_rol",
            "id_estado_usuario",
            "nombre_completo",
            "dni",
            "username",
            "password",
            "telefono",
            "email",
            "direccion",
            "numero_intentos"
    );

    // Ajusta según tu catálogo
    private static final int ESTADO_ACTIVO_ID = 1;

    /* =======================
       Helpers internos
       ======================= */
    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setDni(rs.getString("dni"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password")); // hash o texto plano (temporal)
        u.setTelefono(rs.getString("telefono"));
        u.setEmail(rs.getString("email"));
        u.setDireccion(rs.getString("direccion"));
        u.setNumeroIntentos(rs.getInt("numero_intentos"));

        Rol rol = new Rol();
        rol.setIdRol(rs.getInt("id_rol"));
        u.setIdRol(rol);

        EstadoUsuario estado = new EstadoUsuario();
        estado.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
        u.setIdEstadoUsuario(estado);

        return u;
    }

    private void setNullableInt(PreparedStatement pst, int idx, Integer value) throws SQLException {
        if (value == null) {
            pst.setNull(idx, Types.INTEGER);
        } else {
            pst.setInt(idx, value);
        }
    }

    private boolean looksLikeBCrypt(String s) {
        return s != null && (s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$"));
    }

    /* =======================
       CRUD
       ======================= */
    @Override
    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT " + COLS + " FROM usuarios";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public void insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios "
                + "(id_rol, id_estado_usuario, nombre_completo, dni, username, password, telefono, email, direccion, numero_intentos) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setNullableInt(pst, 1, usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
            setNullableInt(pst, 2, usuario.getIdEstadoUsuario() != null ? usuario.getIdEstadoUsuario().getIdEstadoUsuario() : null);
            pst.setString(3, usuario.getNombreCompleto());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());

            String rawOrHash = usuario.getPassword();
            String toStore = looksLikeBCrypt(rawOrHash) ? rawOrHash : BCrypt.hashpw(rawOrHash, BCrypt.gensalt());
            pst.setString(6, toStore);

            pst.setString(7, usuario.getTelefono());
            pst.setString(8, usuario.getEmail());
            pst.setString(9, usuario.getDireccion());
            pst.setInt(10, usuario.getNumeroIntentos()); // Asegúrate de poner 3 en el servlet de registro

            pst.executeUpdate();

            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setIdUsuario(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Usuario leer(int id) throws SQLException {
        String sql = "SELECT " + COLS + " FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET "
                + "id_rol = ?, id_estado_usuario = ?, nombre_completo = ?, dni = ?, username = ?, password = ?, "
                + "telefono = ?, email = ?, direccion = ?, numero_intentos = ? "
                + "WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            setNullableInt(pst, 1, usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
            setNullableInt(pst, 2, usuario.getIdEstadoUsuario() != null ? usuario.getIdEstadoUsuario().getIdEstadoUsuario() : null);

            pst.setString(3, usuario.getNombreCompleto());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());

            String rawOrHash = usuario.getPassword();
            String toStore = looksLikeBCrypt(rawOrHash) ? rawOrHash : BCrypt.hashpw(rawOrHash, BCrypt.gensalt());
            pst.setString(6, toStore);

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
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /* =======================
       Auth / Intentos / Util
       ======================= */
    // Traer usuario por username (para leer intentos y estado)
    public Usuario getByUsername(String username) throws SQLException {
        String sql = "SELECT " + COLS + " FROM usuarios WHERE username = ? LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Valida credenciales y retorna el Usuario (o null). Requiere estado
     * ACTIVO. BCrypt si el almacenado parece hash; fallback temporal a texto
     * plano.
     */
    public Usuario validateUser(String username, String plainPassword) throws SQLException {
        String sql = "SELECT " + COLS + " FROM usuarios WHERE username = ? AND id_estado_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setInt(2, ESTADO_ACTIVO_ID);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Usuario u = mapRow(rs);
                String stored = u.getPassword();

                boolean ok;
                if (looksLikeBCrypt(stored)) {
                    ok = BCrypt.checkpw(plainPassword, stored);
                } else {
                    ok = plainPassword != null && plainPassword.equals(stored);
                }
                return ok ? u : null;
            }
        }
    }

    // Decrementa intentos (no baja de 0)
    public void aumentarIntentos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios "
                + "SET numero_intentos = numero_intentos + 1 "
                + "WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        }
    }
    // (Alternativa si tu motor no soporta GREATEST)
    // SET numero_intentos = CASE WHEN numero_intentos > 0 THEN numero_intentos - 1 ELSE 0 END

    // Resetea a 3 tras login correcto
    public void resetearIntentos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET numero_intentos = 0 WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        }
    }

    // Bloqueo por estado (p.ej., 2 = BLOQUEADO)
    public void bloquearUsuario(int idUsuario, int estadoBloqueadoId) throws SQLException {
        String sql = "UPDATE usuarios SET id_estado_usuario = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, estadoBloqueadoId);
            pst.setInt(2, idUsuario);
            pst.executeUpdate();
        }
    }
}
