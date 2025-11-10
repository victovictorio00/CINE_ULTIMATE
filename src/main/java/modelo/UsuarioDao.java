package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDao implements DaoCrud<Usuario> {

    // Columnas explícitas (evita SELECT *)
    private static final String COLS = String.join(",", "id_usuario", "id_rol", "id_estado_usuario", "nombre_completo",
            "dni", "username", "password", "telefono", "email", "direccion", "numero_intentos");

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
        u.setPassword(rs.getString("password"));
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

    //  Nuevo método: lista con nombres de Rol y Estado
    public List<Usuario> listarConDetalles() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre_completo, u.dni, u.username, u.password, "
                + "u.telefono, u.email, u.direccion, u.numero_intentos, "
                + "r.id_rol, r.nombre AS nombre_rol, "
                + "e.id_estado_usuario, e.nombre AS nombre_estado "
                + "FROM usuarios u "
                + "LEFT JOIN roles r ON u.id_rol = r.id_rol "
                + "LEFT JOIN estado_usuarios e ON u.id_estado_usuario = e.id_estado_usuario";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setDni(rs.getString("dni"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setTelefono(rs.getString("telefono"));
                u.setEmail(rs.getString("email"));
                u.setDireccion(rs.getString("direccion"));
                u.setNumeroIntentos(rs.getInt("numero_intentos"));

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("nombre_rol"));
                u.setIdRol(rol);

                EstadoUsuario estado = new EstadoUsuario();
                estado.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
                estado.setNombre(rs.getString("nombre_estado"));
                u.setIdEstadoUsuario(estado);

                lista.add(u);
            }
        }
        return lista;
    }

    @Override
    public void insertar(Usuario usuario) throws SQLException {
        String rawOrHash = usuario.getPassword();
        String hash = looksLikeBCrypt(rawOrHash) ? rawOrHash
                : BCrypt.hashpw(rawOrHash, BCrypt.gensalt());

        String sql = "INSERT INTO usuarios (id_rol, id_estado_usuario, nombre_completo, dni, username, password, "
                + "telefono, email, direccion, numero_intentos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, usuario.getIdRol().getIdRol());
            pst.setInt(2, usuario.getIdEstadoUsuario().getIdEstadoUsuario());
            pst.setString(3, usuario.getNombreCompleto());
            pst.setString(4, usuario.getDni());
            pst.setString(5, usuario.getUsername());
            pst.setString(6, hash);
            pst.setString(7, usuario.getTelefono());
            pst.setString(8, usuario.getEmail());
            pst.setString(9, usuario.getDireccion());
            pst.setInt(10, usuario.getNumeroIntentos());

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
    private static final int LIMITE_INTENTOS = 3;

    public Usuario getByUsername(String username) throws SQLException {
        String sql = "SELECT " + COLS + " FROM usuarios WHERE username = ? LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

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

    //no lo usa la bd, es creada por si se necesita usar después
    public void aumentarIntentos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET numero_intentos = numero_intentos + 1 WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        }
    }

    public void resetearIntentos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET numero_intentos = 0 WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        }
    }

    //no lo usa la bd, es creada por si se necesita usar después
    public void bloquearUsuario(int idUsuario, int estadoBloqueadoId) throws SQLException {
        String sql = "UPDATE usuarios SET id_estado_usuario = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, estadoBloqueadoId);
            pst.setInt(2, idUsuario);
            pst.executeUpdate();
        }
    }

    //no lo usa la bd, es creada por si se necesita usar después
    public boolean existeUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public void registrarIntentoFallido(int idUsuario) throws SQLException {
        String sqlUpdate = "UPDATE usuarios SET numero_intentos = numero_intentos + 1 WHERE id_usuario = ?";
        String sqlCheck = "SELECT numero_intentos FROM usuarios WHERE id_usuario = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement psUpd = con.prepareStatement(sqlUpdate); PreparedStatement psChk = con.prepareStatement(sqlCheck)) {

            // incrementar
            psUpd.setInt(1, idUsuario);
            psUpd.executeUpdate();

            // consultar
            psChk.setInt(1, idUsuario);
            try (ResultSet rs = psChk.executeQuery()) {
                if (rs.next() && rs.getInt(1) >= LIMITE_INTENTOS) {
                    bloquearUsuario(idUsuario, 2); // 2 = bloqueado
                }
            }
        }
    }

    //no lo usa la bd, es creada por si se necesita usar después
    public boolean existeDNI(String dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE dni = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public List<Rol> listarRoles() throws SQLException {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre FROM roles";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt("id_rol"));
                r.setNombre(rs.getString("nombre"));
                lista.add(r);
            }
        }
        return lista;
    }

    public List<EstadoUsuario> listarEstados() throws SQLException {
        List<EstadoUsuario> lista = new ArrayList<>();
        String sql = "SELECT id_estado_usuario, nombre FROM estado_usuarios";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                EstadoUsuario e = new EstadoUsuario();
                e.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
                e.setNombre(rs.getString("nombre"));
                lista.add(e);
            }
        }
        return lista;
    }
    public void actualizarDatos(int idUsuario, String nombreCompleto, String telefono, String email, String direccion) {
        String sql = "UPDATE usuarios SET nombre_completo=?, telefono=?, email=?, direccion=? WHERE id_usuario=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreCompleto);
            ps.setString(2, telefono);
            ps.setString(3, email);
            ps.setString(4, direccion);
            ps.setInt(5, idUsuario);

            int filas = ps.executeUpdate();
            System.out.println("→ Filas actualizadas: " + filas); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Usuario leerPorId(int idUsuario) {
        Usuario u = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombreCompleto(rs.getString("nombre_completo"));
                    u.setDni(rs.getString("dni"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setEmail(rs.getString("email"));
                    u.setDireccion(rs.getString("direccion"));
                    u.setNumeroIntentos(rs.getInt("numero_intentos"));
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    u.setIdRol(rol);
                    EstadoUsuario est = new EstadoUsuario();
                    est.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
                    u.setIdEstadoUsuario(est);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }
    public void actualizarPassword(int idUsuario, String hashedPassword) throws SQLException {
        String sql = "UPDATE usuarios SET password = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, idUsuario);
            int filas = ps.executeUpdate();
            System.out.println("Filas afectadas en actualizarPassword(): " + filas);
        }
    }
}
