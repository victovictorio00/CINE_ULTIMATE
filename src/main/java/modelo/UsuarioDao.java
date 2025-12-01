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
    String sql = "{CALL listarUsuarios()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

        while (rs.next()) {
            lista.add(mapRow(rs)); // mapea cada fila a Usuario
        }
    }
    return lista;
}


    //  Nuevo método: lista con nombres de Rol y Estado
   public List<Usuario> listarConDetalles() throws SQLException {
    List<Usuario> lista = new ArrayList<>();
    String sql = "{CALL listarUsuariosConDetalles()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

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
    String hash = looksLikeBCrypt(rawOrHash) ? rawOrHash : BCrypt.hashpw(rawOrHash, BCrypt.gensalt());

    String sql = "{CALL insertarUsuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, usuario.getIdRol().getIdRol());
        cst.setInt(2, usuario.getIdEstadoUsuario().getIdEstadoUsuario());
        cst.setString(3, usuario.getNombreCompleto());
        cst.setString(4, usuario.getDni());
        cst.setString(5, usuario.getUsername());
        cst.setString(6, hash);
        cst.setString(7, usuario.getTelefono());
        cst.setString(8, usuario.getEmail());
        cst.setString(9, usuario.getDireccion());
        cst.setInt(10, usuario.getNumeroIntentos());

        cst.registerOutParameter(11, java.sql.Types.INTEGER);

        cst.executeUpdate();

        usuario.setIdUsuario(cst.getInt(11));
    }
}


   @Override
public Usuario leer(int id) throws SQLException {
    Usuario u = null;
    String sql = "{CALL obtenerUsuarioPorId(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);

        try (ResultSet rs = cst.executeQuery()) {
            if (rs.next()) {
                u = mapRow(rs); // mapea todos los campos del ResultSet a Usuario
            }
        }
    }

    return u;
}


   @Override
public void editar(Usuario usuario) throws SQLException {
    String sql = "{CALL editarUsuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, usuario.getIdUsuario());
        cst.setObject(2, usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
        cst.setObject(3, usuario.getIdEstadoUsuario() != null ? usuario.getIdEstadoUsuario().getIdEstadoUsuario() : null);

        cst.setString(4, usuario.getNombreCompleto());
        cst.setString(5, usuario.getDni());
        cst.setString(6, usuario.getUsername());

        String rawOrHash = usuario.getPassword();
        String toStore = looksLikeBCrypt(rawOrHash) ? rawOrHash : BCrypt.hashpw(rawOrHash, BCrypt.gensalt());
        cst.setString(7, toStore);

        cst.setString(8, usuario.getTelefono());
        cst.setString(9, usuario.getEmail());
        cst.setString(10, usuario.getDireccion());
        cst.setInt(11, usuario.getNumeroIntentos());

        cst.executeUpdate();
    }
}


   @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarUsuario(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);
        cst.execute();
    }
}


    /* =======================
       Auth / Intentos / Util
       ======================= */
    private static final int LIMITE_INTENTOS = 3;

   public Usuario getByUsername(String username) throws SQLException {
    Usuario u = null;
    String sql = "{CALL obtenerUsuarioPorUsername(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, username);

        try (ResultSet rs = cst.executeQuery()) {
            if (rs.next()) {
                u = mapRow(rs); // mapea todos los campos del ResultSet a Usuario
            }
        }
    }
    return u;
}


  public Usuario validateUser(String username, String password) throws SQLException {
    String sql = "{CALL obtenerUsuarioActivo(?, ?)}";
    Usuario u = null;

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, username);
        cst.setInt(2, ESTADO_ACTIVO_ID);

        try (ResultSet rs = cst.executeQuery()) {
            if (!rs.next()) return null;

            u = mapRow(rs); // mapea todos los campos del ResultSet a Usuario
            String stored = u.getPassword(); // bcrypt en la DB

            if (stored == null || password == null) return null;

            // validar bcrypt
            if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                return BCrypt.checkpw(password, stored) ? u : null;
            }

            // soporte legacy plain text (opcional)
            return password.equals(stored) ? u : null;
        }
    }
}




    //no lo usa la bd, es creada por si se necesita usar después
public void aumentarIntentos(int idUsuario) throws SQLException {
    String sql = "{CALL aumentarIntentosUsuario(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);
        cst.execute();
    }
}


   public void resetearIntentos(int idUsuario) throws SQLException {
    String sql = "{CALL resetearIntentosUsuario(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);
        cst.execute();
    }
}


    //no lo usa la bd, es creada por si se necesita usar después
    public void bloquearUsuario(int idUsuario, int estadoBloqueadoId) throws SQLException {
    String sql = "{CALL bloquearUsuario(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);
        cst.setInt(2, estadoBloqueadoId);

        cst.execute();
    }
}


    //no lo usa la bd, es creada por si se necesita usar después
   public boolean existeUsername(String username) throws SQLException {
    String sql = "{CALL existeUsernameUsuario(?, ?)}";
    boolean existe = false;

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, username);
        cst.registerOutParameter(2, java.sql.Types.BOOLEAN);

        cst.execute();

        existe = cst.getBoolean(2);
    }

    return existe;
}


   public void registrarIntentoFallido(int idUsuario) throws SQLException {
    String sql = "{CALL registrarIntentoFallidoUsuario(?, ?)}";
    int LIMITE_INTENTOS = 3;

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);
        cst.setInt(2, LIMITE_INTENTOS);

        cst.execute();
    }
}


    //no lo usa la bd, es creada por si se necesita usar después
   public boolean existeDNI(String dni) throws SQLException {
    String sql = "{CALL existeDNIUsuario(?, ?)}";
    boolean existe = false;

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, dni);
        cst.registerOutParameter(2, java.sql.Types.BOOLEAN);

        cst.execute();

        existe = cst.getBoolean(2);
    }

    return existe;
}


    public List<Rol> listarRoles() throws SQLException {
    List<Rol> lista = new ArrayList<>();
    String sql = "{CALL listarRolesUsuarios()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

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
    String sql = "{CALL listarEstadosUsuarios()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

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
    String sql = "{CALL actualizarDatosUsuario(?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);
        cst.setString(2, nombreCompleto);
        cst.setString(3, telefono);
        cst.setString(4, email);
        cst.setString(5, direccion);

        int filas = cst.executeUpdate();
        System.out.println("→ Filas actualizadas: " + filas);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public Usuario leerPorId(int idUsuario) {
    Usuario u = null;
    String sql = "{CALL leerUsuarioPorId(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);

        try (ResultSet rs = cst.executeQuery()) {
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
    String sql = "{CALL actualizarPasswordUsuario(?, ?)}";

    try (Connection conn = Conexion.getConnection();
         CallableStatement cst = conn.prepareCall(sql)) {

        cst.setString(1, hashedPassword);
        cst.setInt(2, idUsuario);

        int filas = cst.executeUpdate();
        System.out.println("Filas afectadas en actualizarPassword(): " + filas);
    }
}

}
