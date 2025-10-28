package Controlador.Cliente;

import Conexion.Conexion;
import modelo.Asiento;
import modelo.AsientoDao;
import modelo.Funcion;
import modelo.FuncionDao;
import modelo.Producto;
import modelo.ProductoDao;
import modelo.ReservaTemporal;
import modelo.Pelicula;
import modelo.PeliculaDao;
import modelo.Sala;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;

/**
 * ClienteServlet - controlador del flujo de compra del cliente
 */
@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final FuncionDao funcionDao = new FuncionDao();
    private final AsientoDao asientoDao = new AsientoDao();
    private final ProductoDao productoDao = new ProductoDao();
    private final PeliculaDao peliculaDao = new PeliculaDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "reservar":
                    accionReservar(req, resp);
                    break;
                case "guardarButacas":
                    accionGuardarButacas(req, resp);
                    break;
                case "guardarDulceria":
                    accionGuardarDulceria(req, resp);
                    break;
                case "metodoPago":
                    accionMetodoPago(req, resp);
                    break;
                case "confirmarCompraPage":
                    req.getRequestDispatcher("/Cliente/ConfirmacionCompra.jsp").forward(req, resp);
                    break;
                case "confirmarCompra":
                    accionConfirmarCompra(req, resp);
                    break;
                case "comprobante":
                    accionComprobante(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/CarteleraServlet");
            }
        } catch (Exception e) {
            // enviar a error.jsp con traza
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            req.setAttribute("error", e.getMessage() != null ? e.getMessage() : "Error interno");
            req.setAttribute("stacktrace", sw.toString());
            try {
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            } catch (Exception ignored) {}
        }
    }

    /* ------------------ acción reservar (conversión a modelo.Asiento) ------------------ */
  /* ------------------ acción reservar (conversión a modelo.Asiento) ------------------ */
/* ------------------ acción reservar (robusto: asegurar Sala) ------------------ */
private void accionReservar(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    String idFuncStr = req.getParameter("idFuncion");
    String idPelStr = req.getParameter("id");

    if (idFuncStr == null || idFuncStr.trim().isEmpty()) {
        String redirect = req.getContextPath() + "/DetallePelicula.jsp";
        if (idPelStr != null && !idPelStr.trim().isEmpty()) redirect += "?id=" + URLEncoder.encode(idPelStr, "UTF-8");
        redirect += (redirect.contains("?") ? "&" : "?") + "error=" + URLEncoder.encode("Seleccione un horario", "UTF-8");
        resp.sendRedirect(redirect);
        return;
    }

    final int idFuncion;
    try {
        idFuncion = Integer.parseInt(idFuncStr.trim());
    } catch (NumberFormatException ex) {
        resp.sendRedirect(req.getContextPath() + "/DetallePelicula.jsp?error=" + URLEncoder.encode("ID de función inválido", "UTF-8"));
        return;
    }

    // obtener función
    Funcion f = funcionDao.obtener(idFuncion);
    if (f == null) {
        resp.sendRedirect(req.getContextPath() + "/DetallePelicula.jsp?error=" + URLEncoder.encode("Función no encontrada", "UTF-8"));
        return;
    }

    // intentar obtener idPelicula desde parámetro o desde la función
    int idPelicula = 0;
    if (idPelStr != null && !idPelStr.trim().isEmpty()) {
        try { idPelicula = Integer.parseInt(idPelStr.trim()); } catch (NumberFormatException ignore) {}
    }
    if (idPelicula == 0) {
        try {
            // si la función tiene getPelicula y ésta no es null, usarla
            if (f.getPelicula() != null) idPelicula = f.getPelicula().getIdPelicula();
            else {
                // intentar por reflexión buscar getIdPelicula
                try {
                    Method m = f.getClass().getMethod("getIdPelicula");
                    Object v = m.invoke(f);
                    if (v != null) idPelicula = Integer.parseInt(String.valueOf(v));
                } catch (NoSuchMethodException ignore) { /* no hay getIdPelicula */ }
            }
        } catch (Throwable ignored) {}
    }

    Pelicula pelicula = peliculaDao.leer(idPelicula);
    if (pelicula == null) pelicula = (f.getPelicula() != null) ? f.getPelicula() : new Pelicula();

    // ---------- SALA: intentar obtenerla robustamente ----------
    Sala sala = null;
    try {
        // primer intento: función ya trae la sala
        sala = f.getSala();
    } catch (Throwable ignored) {}

    Integer salaIdFromFunc = null;
    if (sala == null) {
        // intentar extraer idSala por reflexión desde Funcion
        try {
            Method mid = f.getClass().getMethod("getIdSala");
            Object o = mid.invoke(f);
            if (o != null) salaIdFromFunc = Integer.parseInt(String.valueOf(o));
        } catch (NoSuchMethodException ns) {
            // intentar otras variantes
            try {
                Method mid2 = f.getClass().getMethod("getSalaId");
                Object o2 = mid2.invoke(f);
                if (o2 != null) salaIdFromFunc = Integer.parseInt(String.valueOf(o2));
            } catch (Throwable ignore) {}
        } catch (Throwable ignore) {}
    } else {
        try {
            // si sala no es null pero queremos asegurar id, intentar obtener su id
            Method gm = sala.getClass().getMethod("getIdSala");
            Object o = gm.invoke(sala);
            if (o != null) salaIdFromFunc = Integer.parseInt(String.valueOf(o));
        } catch (Throwable ignored) {}
    }

    // Si aún no tenemos Sala pero sí un id, cargar sala desde DB
    if (sala == null && (salaIdFromFunc == null || salaIdFromFunc == 0)) {
        // intentar obtener idSala desde la función (otra forma)
        try {
            // si Funcion tiene getSala() que devuelve un Map o similar, manejarlo
            Method getSalaM = f.getClass().getMethod("getSala");
            Object salaObj = getSalaM.invoke(f);
            if (salaObj != null && salaObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String,Object> sm = (Map<String,Object>) salaObj;
                if (sm.containsKey("id") || sm.containsKey("id_sala")) {
                    Object vid = sm.containsKey("id") ? sm.get("id") : sm.get("id_sala");
                    if (vid != null) salaIdFromFunc = Integer.parseInt(String.valueOf(vid));
                }
            }
        } catch (Throwable ignored) {}
    }

    // Si tenemos salaIdFromFunc > 0, cargar Sala desde la tabla 'salas'
    if (sala == null && salaIdFromFunc != null && salaIdFromFunc > 0) {
        Connection con = null;
        try {
            con = Conexion.getConnection();
            String sql = "SELECT * FROM salas WHERE id_sala = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, salaIdFromFunc);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        sala = new Sala();
                        // establecer id (manejar varias firmas)
                        try { sala.getClass().getMethod("setIdSala", int.class).invoke(sala, rs.getInt("id_sala")); } catch (NoSuchMethodException e1) {
                            try { sala.getClass().getMethod("setId", int.class).invoke(sala, rs.getInt("id_sala")); } catch (Throwable ignore) {}
                        }
                        // establecer nombre/descripción si existen
                        try {
                            if (hasColumn(rs, "nombre")) {
                                try { sala.getClass().getMethod("setNombre", String.class).invoke(sala, rs.getString("nombre")); } catch (NoSuchMethodException ignore) {}
                            } else if (hasColumn(rs, "nombre_sala")) {
                                try { sala.getClass().getMethod("setNombre", String.class).invoke(sala, rs.getString("nombre_sala")); } catch (NoSuchMethodException ignore) {}
                            }
                        } catch (Throwable ignored) {}
                    }
                }
            }
        } catch (Throwable ignored) {
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
    }

    // si no conseguimos sala, intentar construir un objeto Sala mínimo con el id conocido
    if (sala == null && salaIdFromFunc != null && salaIdFromFunc > 0) {
        try {
            sala = new Sala();
            try { sala.getClass().getMethod("setIdSala", int.class).invoke(sala, salaIdFromFunc); }
            catch (NoSuchMethodException e1) { try { sala.getClass().getMethod("setId", int.class).invoke(sala, salaIdFromFunc); } catch (Throwable ignore) {} }
        } catch (Throwable ignored) {}
    }

    // determinar idSala final (para ReservaTemporal)
    int idSalaParaRt = 0;
    try {
        if (sala != null) {
            try {
                Method gm = sala.getClass().getMethod("getIdSala");
                Object o = gm.invoke(sala);
                if (o != null) idSalaParaRt = Integer.parseInt(String.valueOf(o));
            } catch (NoSuchMethodException ex) {
                try {
                    Method gm2 = sala.getClass().getMethod("getId");
                    Object o2 = gm2.invoke(sala);
                    if (o2 != null) idSalaParaRt = Integer.parseInt(String.valueOf(o2));
                } catch (Throwable ignore) {}
            } catch (Throwable ignore) {}
        }
        if (idSalaParaRt == 0 && salaIdFromFunc != null) idSalaParaRt = salaIdFromFunc;
        // fallback: intentar obtener id sala desde la funcion por reflexión
        if (idSalaParaRt == 0) {
            try {
                Method gm = f.getClass().getMethod("getIdSala");
                Object o = gm.invoke(f);
                if (o != null) idSalaParaRt = Integer.parseInt(String.valueOf(o));
            } catch (Throwable ignored) {}
        }
    } catch (Throwable ignored) {}

    // ---------- precio entrada y fecha ----------
    double precioEntrada = 0.0;
    if (pelicula != null) {
        try {
            Object val = pelicula.getClass().getMethod("getPrecio").invoke(pelicula);
            if (val instanceof Number) precioEntrada = ((Number) val).doubleValue();
        } catch (Throwable ignore) {}
    }
    java.util.Date fecha = f.getFechaInicio();

    // crear ReservaTemporal con idFuncion, idPelicula, precioEntrada, fecha y idSalaParaRt
    ReservaTemporal rt = new ReservaTemporal(idFuncion, idPelicula, precioEntrada, fecha, idSalaParaRt);
    HttpSession session = req.getSession();
    session.setAttribute("reservaTemporal", rt);

    // ---------- cargar asientos como antes (allow dao or db) ----------
    List<Object> asientosRaw = tryLoadAsientosViaDao(idSalaParaRt, rt.getIdFuncion());
    if (asientosRaw == null || asientosRaw.isEmpty()) asientosRaw = loadAsientosFromDb(idSalaParaRt, rt.getIdFuncion());

    List<Asiento> asientos = new ArrayList<>();
    if (asientosRaw != null) {
        for (Object o : asientosRaw) {
            if (o == null) continue;
            if (o instanceof Asiento) { asientos.add((Asiento) o); continue; }
            if (o instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String,Object> m = (Map<String,Object>) o;
                Asiento a = new Asiento();
                try {
                    if (m.containsKey("id")) {
                        Object idv = m.get("id");
                        if (idv != null) {
                            int idInt = Integer.parseInt(String.valueOf(idv));
                            try { a.getClass().getMethod("setIdAsiento", int.class).invoke(a, idInt); }
                            catch (NoSuchMethodException ex1) {
                                try { a.getClass().getMethod("setId", int.class).invoke(a, idInt); } catch (Throwable ignore) {}
                            }
                        }
                    } else if (m.containsKey("id_asiento")) {
                        Object idv = m.get("id_asiento");
                        if (idv != null) {
                            int idInt = Integer.parseInt(String.valueOf(idv));
                            try { a.getClass().getMethod("setIdAsiento", int.class).invoke(a, idInt); }
                            catch (NoSuchMethodException ex1) {
                                try { a.getClass().getMethod("setId", int.class).invoke(a, idInt); } catch (Throwable ignore) {}
                            }
                        }
                    }
                } catch (Throwable ignore) {}
                try {
                    String codigo = null;
                    if (m.containsKey("codigo") && m.get("codigo") != null) codigo = String.valueOf(m.get("codigo"));
                    else if (m.containsKey("codigo_asiento") && m.get("codigo_asiento") != null) codigo = String.valueOf(m.get("codigo_asiento"));
                    if (codigo != null) try { a.getClass().getMethod("setCodigo", String.class).invoke(a, codigo); } catch (Throwable ignore) {}
                } catch (Throwable ignore) {}

                try {
                    boolean ocupado = false;
                    if (m.containsKey("ocupado") && m.get("ocupado") != null) {
                        Object ocu = m.get("ocupado");
                        if (ocu instanceof Boolean) ocupado = (Boolean) ocu;
                        else {
                            String s = String.valueOf(ocu).trim();
                            ocupado = "1".equals(s) || "true".equalsIgnoreCase(s);
                        }
                    } else if (m.containsKey("estado") && m.get("estado") != null) {
                        Object ocu = m.get("estado");
                        if (ocu instanceof Boolean) ocupado = (Boolean) ocu;
                        else {
                            String s = String.valueOf(ocu).trim();
                            ocupado = "1".equals(s) || "true".equalsIgnoreCase(s);
                        }
                    }
                    try { a.getClass().getMethod("setOcupado", boolean.class).invoke(a, ocupado); }
                    catch (NoSuchMethodException ex) { try { a.getClass().getMethod("setOcupado", Boolean.class).invoke(a, Boolean.valueOf(ocupado)); } catch (Throwable ignore) {} }
                } catch (Throwable ignore) {}

                asientos.add(a);
            }
        }
    }

    // pasar atributos a JSP
    req.setAttribute("asientos", asientos);
    req.setAttribute("pelicula", pelicula);
    req.setAttribute("sala", sala);
    req.setAttribute("funcion", f);

    String genero = "";
    try { if (pelicula!=null && pelicula.getIdGenero()!=null) genero = pelicula.getIdGenero().getNombre(); } catch (Throwable ignored){}
    String duracionMin = "";
    try {
        try { Method m = pelicula.getClass().getMethod("getDuracion"); Object d = m.invoke(pelicula); if (d!=null) duracionMin = String.valueOf(d); }
        catch (NoSuchMethodException ex) {
            try { Method m2 = pelicula.getClass().getMethod("getDuracionMin"); Object d2 = m2.invoke(pelicula); if (d2!=null) duracionMin = String.valueOf(d2); }
            catch (NoSuchMethodException ignore) {}
        }
    } catch (Throwable ignored){}
    req.setAttribute("duracionMin", duracionMin);
    req.setAttribute("genero", genero);
    req.setAttribute("precioButaca", precioEntrada);

    req.getRequestDispatcher("/Cliente/SeleccionAsiento.jsp").forward(req, resp);
}

// helper: comprobar si ResultSet contiene la columna
private boolean hasColumn(ResultSet rs, String columnName) {
    try {
        rs.findColumn(columnName);
        return true;
    } catch (SQLException ex) { return false; }
}



    /* ------------------ helpers and DB utils ------------------ */

    private static class MethodSignature { String name; Class<?>[] paramTypes; MethodSignature(String n, Class<?>... p){name=n; paramTypes=p;} }

    @SuppressWarnings({"unchecked","rawtypes"})
    private List<Object> tryLoadAsientosViaDao(int idSala, int idFuncion) {
        try {
            Object dao = asientoDao;
            Class<?> daoClass = dao.getClass();
            List<MethodSignature> candidates = Arrays.asList(
                    new MethodSignature("listarPorSalaYFuncion", int.class, int.class),
                    new MethodSignature("listarPorSala", int.class),
                    new MethodSignature("listar", int.class),
                    new MethodSignature("obtenerPorSala", int.class),
                    new MethodSignature("obtenerPorFuncionYSala", int.class, int.class)
            );
            for (MethodSignature ms : candidates) {
                try {
                    Method m = findMethod(daoClass, ms.name, ms.paramTypes);
                    if (m!=null) {
                        Object res = (ms.paramTypes.length==2) ? m.invoke(dao, idSala, idFuncion) : m.invoke(dao, idSala);
                        if (res instanceof List) return (List<Object>) res;
                    }
                } catch (Throwable ignored){}
            }
        } catch (Throwable ignored){}
        return new ArrayList<>();
    }

    private Method findMethod(Class<?> cls, String name, Class<?>... paramTypes){
        try {
            for (Method m : cls.getMethods()) {
                if (!m.getName().equals(name)) continue;
                Class<?>[] pts = m.getParameterTypes();
                if (pts.length != paramTypes.length) continue;
                boolean ok = true;
                for (int i=0;i<pts.length;i++) {
                    if (!paramTypes[i].isAssignableFrom(pts[i]) && !pts[i].isAssignableFrom(paramTypes[i])) { ok = false; break; }
                }
                if (ok) return m;
            }
        } catch (Throwable ignored){}
        return null;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private List<Object> loadAsientosFromDb(int idSala, int idFuncion) {
        List<Object> result = new ArrayList<>();
        Connection con = null;
        String sqlWithSub = "SELECT a.id_asiento, a.codigo, CASE WHEN EXISTS (SELECT 1 FROM detalle_ventas dv WHERE dv.id_asiento = a.id_asiento AND dv.id_funcion = ?) THEN 1 ELSE 0 END AS ocupado FROM asientos a WHERE a.id_sala = ? ORDER BY a.codigo";
        String sqlBasic = "SELECT a.id_asiento, a.codigo FROM asientos a WHERE a.id_sala = ? ORDER BY a.codigo";
        try {
            con = Conexion.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sqlWithSub)) {
                ps.setInt(1, idFuncion);
                ps.setInt(2, idSala);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String,Object> m = new HashMap<>();
                        m.put("id", rs.getInt("id_asiento"));
                        m.put("codigo", rs.getString("codigo"));
                        m.put("ocupado", rs.getInt("ocupado") != 0);
                        result.add(m);
                    }
                }
            } catch (Throwable e) {
                try (PreparedStatement ps = con.prepareStatement(sqlBasic)) {
                    ps.setInt(1, idSala);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Map<String,Object> m = new HashMap<>();
                            m.put("id", rs.getInt("id_asiento"));
                            m.put("codigo", rs.getString("codigo"));
                            m.put("ocupado", false);
                            result.add(m);
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignored){}
        }
        return result;
    }

    /* ------------------ guardar butacas/dulcería/método ------------------ */
    private void accionGuardarButacas(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String butacas = req.getParameter("butacas");
        List<String> list = new ArrayList<>();
        if (butacas != null && !butacas.trim().isEmpty()) {
            for (String s : butacas.split(",")) {
                String t = s.trim();
                if (!t.isEmpty()) list.add(t);
            }
        }
        HttpSession session = req.getSession();
        session.setAttribute("butacasSeleccionadas", list);
        String total = req.getParameter("total");
        if (total != null && !total.trim().isEmpty()) session.setAttribute("totalEntradas", total);
        resp.sendRedirect(req.getContextPath() + "/DulceriaServlet");
    }

    private void accionGuardarDulceria(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<Integer,Integer> carrito = new LinkedHashMap<>();
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String p = params.nextElement();
            if (p.startsWith("product_")) {
                try {
                    int pid = Integer.parseInt(p.substring(8));
                    int qty = Integer.parseInt(req.getParameter(p));
                    if (qty > 0) carrito.put(pid, qty);
                } catch (NumberFormatException ignore) {}
            }
        }
        HttpSession session = req.getSession();
        session.setAttribute("productosSeleccionados", carrito);
        session.setAttribute("carritoDulceria", carrito);
        resp.sendRedirect(req.getContextPath() + "/ClienteServlet?action=metodoPago");
    }

    private void accionMetodoPago(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            req.getRequestDispatcher("/Cliente/MetodoPago.jsp").forward(req, resp);
            return;
        }
        String metodo = req.getParameter("metodo");
        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");
        Map<String,String> datos = new HashMap<>();
        datos.put("metodo", metodo != null ? metodo : "");
        datos.put("nombre", nombre != null ? nombre : "");
        datos.put("email", email != null ? email : "");
        HttpSession session = req.getSession();
        session.setAttribute("metodoPago", metodo);
        session.setAttribute("clienteDatos", datos);
        resp.sendRedirect(req.getContextPath() + "/ClienteServlet?action=confirmarCompraPage");
    }

    /* ------------------ metadata helpers ------------------ */
    private List<ColumnMeta> getColumnsForTable(Connection con, String tableName) throws SQLException {
        List<ColumnMeta> cols = new ArrayList<>();
        DatabaseMetaData meta = con.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                int dt = rs.getInt("DATA_TYPE");
                String isNullable = rs.getString("IS_NULLABLE");
                String def = rs.getString("COLUMN_DEF");
                cols.add(new ColumnMeta(col, dt, isNullable, def));
            }
        } catch (Throwable ignored){}
        if (cols.isEmpty()) {
            try (ResultSet rs = meta.getColumns(null, null, tableName.toUpperCase(), null)) {
                while (rs.next()) {
                    String col = rs.getString("COLUMN_NAME");
                    int dt = rs.getInt("DATA_TYPE");
                    String isNullable = rs.getString("IS_NULLABLE");
                    String def = rs.getString("COLUMN_DEF");
                    cols.add(new ColumnMeta(col, dt, isNullable, def));
                }
            } catch (Throwable ignored){}
        }
        if (cols.isEmpty()) {
            String sql = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tableName);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String col = rs.getString("COLUMN_NAME");
                        String dtName = rs.getString("DATA_TYPE");
                        int dt = mapSqlTypeNameToJdbc(dtName);
                        String isNullable = rs.getString("IS_NULLABLE");
                        String def = rs.getString("COLUMN_DEFAULT");
                        cols.add(new ColumnMeta(col, dt, isNullable, def));
                    }
                }
            } catch (Throwable ignored){}
        }
        return cols;
    }

    private int mapSqlTypeNameToJdbc(String sqlType) {
        if (sqlType == null) return Types.VARCHAR;
        String s = sqlType.toLowerCase();
        if (s.contains("int")) return Types.INTEGER;
        if (s.contains("bigint")) return Types.BIGINT;
        if (s.contains("decimal") || s.contains("numeric")) return Types.DECIMAL;
        if (s.contains("double") || s.contains("float")) return Types.DOUBLE;
        if (s.contains("date") || s.contains("time") || s.contains("timestamp")) return Types.TIMESTAMP;
        if (s.contains("char") || s.contains("text") || s.contains("varchar")) return Types.VARCHAR;
        return Types.VARCHAR;
    }

    private String detectColumn(Connection con, String tableName, String[] candidates) throws SQLException {
        List<ColumnMeta> cols = getColumnsForTable(con, tableName);
        Map<String,String> lowerToOrig = new HashMap<>();
        for (ColumnMeta cm : cols) lowerToOrig.put(cm.name.toLowerCase(), cm.name);
        for (String cand : candidates) {
            String low = cand.toLowerCase();
            if (lowerToOrig.containsKey(low)) return lowerToOrig.get(low);
        }
        for (ColumnMeta cm : cols) {
            String low = cm.name.toLowerCase();
            for (String cand : candidates) {
                String candNorm = cand.replace("_","").toLowerCase();
                if (low.contains(candNorm) || candNorm.contains(low)) return cm.name;
            }
        }
        for (ColumnMeta cm : cols) {
            String low = cm.name.toLowerCase();
            if (low.contains("funcion") || low.contains("id_func")) return cm.name;
            if (low.contains("usuario") || low.contains("cliente") || low.contains("user")) return cm.name;
            if (low.contains("codigo") || low.contains("asiento")) return cm.name;
            if (low.contains("precio") || low.contains("importe") || low.contains("valor") || low.contains("monto") || low.contains("price")) return cm.name;
            if (low.contains("cantidad") || low.contains("qty")) return cm.name;
            if (low.contains("tipo")) return cm.name;
        }
        return null;
    }

    private String findTableHavingColumns(Connection con, String[] requiredCols) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();
        try (ResultSet tables = meta.getTables(null, null, "%", new String[] {"TABLE"})) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                List<ColumnMeta> colsMeta = getColumnsForTable(con, tableName);
                Set<String> cols = new HashSet<>();
                for (ColumnMeta cm : colsMeta) cols.add(cm.name.toLowerCase());
                boolean ok = true;
                for (String r : requiredCols) if (!cols.contains(r.toLowerCase())) { ok = false; break; }
                if (ok) return tableName;
            }
        }
        return null;
    }

    /* ------------------ confirmar compra (VERSIÓN REVISADA Y ROBUSTA) ------------------ */
    private void accionConfirmarCompra(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        ReservaTemporal rt = (ReservaTemporal) session.getAttribute("reservaTemporal");
        @SuppressWarnings("unchecked")
        List<String> butacas = (List<String>) session.getAttribute("butacasSeleccionadas");
        @SuppressWarnings("unchecked")
        Map<Integer,Integer> productos = (Map<Integer,Integer>) session.getAttribute("productosSeleccionados");
        String metodo = (String) session.getAttribute("metodoPago");

        if (rt == null || butacas == null || butacas.isEmpty()) {
            throw new Exception("Datos incompletos para confirmar la compra.");
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login.jsp");
            return;
        }

        Connection con = null;
        long idVenta = -1;
        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false);

            // ------------------- 1) Calcular totales -------------------
            double totalEntradas = rt.getPrecioEntrada() * butacas.size();
            double totalProductos = 0.0;
            if (productos != null && !productos.isEmpty()) {
                for (Map.Entry<Integer,Integer> e : productos.entrySet()) {
                    Producto p = productoDao.leer(e.getKey());
                    double precio = 0.0;
                    if (p != null) {
                        try { precio = p.getPrecio(); } catch (Throwable ignore) { precio = 0.0; }
                    }
                    totalProductos += precio * e.getValue();
                }
            }
            double total = totalEntradas + totalProductos;

            // ------------------- 2) Insertar en ventas -------------------
            String[] userCandidates = new String[] {"id_usuario","id_cliente","usuario_id","idUsuario","usuario","id_user","id_usuario_cliente"};
            String userColumn = detectColumn(con, "ventas", userCandidates);
            if (userColumn == null) throw new SQLException("No se encontró columna para el id de usuario en la tabla 'ventas'.");

            String[] funcCandidates = new String[] {"id_funcion","id_func","funcion_id","idFuncion","id_function"};
            String ventasFuncCol = detectColumn(con, "ventas", funcCandidates);
            if (ventasFuncCol != null && ventasFuncCol.equalsIgnoreCase(userColumn)) ventasFuncCol = null;

            if (ventasFuncCol != null) {
                String sql = "INSERT INTO ventas (" + userColumn + ", " + ventasFuncCol + ", fecha, total, metodo_pago) VALUES (?, ?, NOW(), ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, rt.getIdFuncion());
                    ps.setDouble(3, total);
                    ps.setString(4, metodo != null ? metodo : "DESCONOCIDO");
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) idVenta = keys.getLong(1); }
                }
            } else {
                String sql = "INSERT INTO ventas (" + userColumn + ", fecha, total, metodo_pago) VALUES (?, NOW(), ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setDouble(2, total);
                    ps.setString(3, metodo != null ? metodo : "DESCONOCIDO");
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) idVenta = keys.getLong(1); }
                }
            }

            if (idVenta == -1) throw new SQLException("No se obtuvo idVenta.");

            // ------------------- 3) Insertar detalle de asientos -------------------
            String detailTable = findTableHavingColumns(con, new String[] {"id_asiento","id_funcion"});
            if (detailTable == null) detailTable = findTableHavingColumns(con, new String[] {"id_asiento","id_venta"});
            if (detailTable == null) throw new SQLException("No se encontró tabla de detalle de ventas.");

            String detalleFuncCol = detectColumn(con, detailTable, funcCandidates);
            if (detalleFuncCol == null) throw new SQLException("No se encontró columna de función en tabla detalle.");

            String[] codigoCandidates = new String[] {"codigo","codigo_asiento","asiento_codigo","asiento","codigoAsiento","codigo_butaca"};
            String[] precioCandidates = new String[] {"precio","precio_unitario","precio_entrada","valor","importe","monto","price","unit_price","subtotal_asiento"};
            String detalleCodigoCol = detectColumn(con, detailTable, codigoCandidates);
            String detallePrecioCol = detectColumn(con, detailTable, precioCandidates);

            boolean includeCodigo = detalleCodigoCol != null && !detalleCodigoCol.trim().isEmpty();
            boolean includePrecio = detallePrecioCol != null && !detallePrecioCol.trim().isEmpty();

            List<String> colsOrder = new ArrayList<>();
            colsOrder.add("id_venta");
            colsOrder.add("id_asiento");
            if (includeCodigo) colsOrder.add(detalleCodigoCol);
            if (includePrecio) colsOrder.add(detallePrecioCol);
            colsOrder.add(detalleFuncCol);

            // Evitar duplicados (normalización)
            List<String> uniqueCols = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            for (String c : colsOrder) {
                String low = c.toLowerCase();
                if (!seen.contains(low)) { seen.add(low); uniqueCols.add(c); }
            }

            StringBuilder sbCols = new StringBuilder();
            StringBuilder sbVals = new StringBuilder();
            for (int i = 0; i < uniqueCols.size(); i++) {
                if (i > 0) { sbCols.append(", "); sbVals.append(", "); }
                sbCols.append(uniqueCols.get(i));
                sbVals.append("?");
            }
            String insertDetalleSql = "INSERT INTO " + detailTable + " (" + sbCols.toString() + ") VALUES (" + sbVals.toString() + ")";

            String selectAsientoSql = "SELECT id_asiento FROM asientos WHERE codigo = ? AND id_sala = ?";
            String checkSql = "SELECT COUNT(*) FROM " + detailTable + " WHERE id_asiento = ? AND " + detalleFuncCol + " = ?";

            try (PreparedStatement psSelect = con.prepareStatement(selectAsientoSql);
                 PreparedStatement psCheck = con.prepareStatement(checkSql);
                 PreparedStatement psInsert = con.prepareStatement(insertDetalleSql)) {

                for (String codigo : butacas) {
                    psSelect.setString(1, codigo);
                    psSelect.setInt(2, rt.getIdSala());
                    int idAsiento = -1;
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) idAsiento = rs.getInt(1);
                        else throw new SQLException("Asiento no encontrado: " + codigo);
                    }

                    psCheck.setInt(1, idAsiento);
                    psCheck.setInt(2, rt.getIdFuncion());
                    try (ResultSet rc = psCheck.executeQuery()) {
                        rc.next();
                        if (rc.getInt(1) > 0) throw new SQLException("Asiento ya reservado: " + codigo);
                    }

                    int idx = 1;
                    for (String col : uniqueCols) {
                        if (col.equalsIgnoreCase("id_venta")) psInsert.setLong(idx++, idVenta);
                        else if (col.equalsIgnoreCase("id_asiento")) psInsert.setInt(idx++, idAsiento);
                        else if (includeCodigo && col.equalsIgnoreCase(detalleCodigoCol)) psInsert.setString(idx++, codigo);
                        else if (includePrecio && col.equalsIgnoreCase(detallePrecioCol)) psInsert.setDouble(idx++, rt.getPrecioEntrada());
                        else if (col.equalsIgnoreCase(detalleFuncCol)) psInsert.setInt(idx++, rt.getIdFuncion());
                        else psInsert.setNull(idx++, Types.NULL);
                    }
                    psInsert.addBatch();
                }
                psInsert.executeBatch();
            }

            // ------------------- 4) Insertar productos (más simple y robusto) -------------------
            if (productos != null && !productos.isEmpty()) {

                // 1) localizar tabla para productos de venta
                String vpTable = findTableHavingColumns(con, new String[] {"id_venta", "id_producto"});
                if (vpTable == null) {
                    // fallback: si existe detalle_ventas lo usaremos (muchos esquemas mezclan productos y asientos allí)
                    List<ColumnMeta> dvCols = getColumnsForTable(con, "detalle_ventas");
                    if (!dvCols.isEmpty()) vpTable = "detalle_ventas";
                }

                if (vpTable == null) {
                    System.err.println("No se encontró tabla para guardar productos de venta. Productos omitidos.");
                } else {
                    List<ColumnMeta> vpCols = getColumnsForTable(con, vpTable);
                    Map<String, ColumnMeta> metaByName = new HashMap<>();
                    for (ColumnMeta cm : vpCols) metaByName.put(cm.name.toLowerCase(), cm);

                    // detectar columnas críticas
                    String[] idProdCandidates = new String[] {"id_producto","producto_id","id_prod","idProduct"};
                    String[] qtyCandidates = new String[] {"cantidad","qty","cantidad_producto","cantidad_prod","cantidad_vendida"};
                    String[] subtotalCandidates = new String[] {"subtotal","subtotal_producto","importe","monto","valor","total_producto","total"};
                    String[] tipoCandidates = new String[] {"tipo_item","tipoitem","tipo","tipo_producto"};

                    String vpIdProd = findColumnName(vpCols, idProdCandidates);
                    String vpQty = findColumnName(vpCols, qtyCandidates);
                    String vpSubtotal = findColumnName(vpCols, subtotalCandidates);
                    String vpTipoCol = findColumnName(vpCols, tipoCandidates);

                    // mejor fallback: si no detectamos vpTipoCol pero existe alguna columna con 'tipo' en su nombre, úsala
                    if (vpTipoCol == null) {
                        for (ColumnMeta cm : vpCols) if (cm.name.toLowerCase().contains("tipo")) { vpTipoCol = cm.name; break; }
                    }

                    // Si no hay columna id_producto, simplemente no podemos insertar productos
                    if (vpIdProd == null) {
                        System.err.println("La tabla '" + vpTable + "' no tiene columna de producto detectada. Productos omitidos.");
                    } else {
                        // Preparar orden de columnas para el insert (solo las que vamos a manejar)
                        List<String> insertCols = new ArrayList<>();
                        // detect id_venta col
                        String idVentaCol = findColumnName(vpCols, new String[] {"id_venta","venta_id","id_venta"} );
                        if (idVentaCol == null) {
                            // buscar cualquier columna que contenga 'venta'
                            for (ColumnMeta cm : vpCols) if (cm.name.toLowerCase().contains("venta")) { idVentaCol = cm.name; break; }
                        }
                        if (idVentaCol == null) throw new SQLException("No detecté columna id_venta en tabla " + vpTable);

                        insertCols.add(idVentaCol);
                        insertCols.add(vpIdProd);
                        if (vpQty != null) insertCols.add(vpQty);
                        if (vpSubtotal != null) insertCols.add(vpSubtotal);
                        if (vpTipoCol != null) insertCols.add(vpTipoCol);

                        // construir SQL dinámico
                        StringBuilder sbc = new StringBuilder(), sbv = new StringBuilder();
                        for (int i = 0; i < insertCols.size(); i++) {
                            if (i > 0) { sbc.append(", "); sbv.append(", "); }
                            sbc.append(insertCols.get(i));
                            sbv.append("?");
                        }
                        String insertProdSql = "INSERT INTO " + vpTable + " (" + sbc.toString() + ") VALUES (" + sbv.toString() + ")";

                        try (PreparedStatement psProd = con.prepareStatement(insertProdSql)) {
                            for (Map.Entry<Integer,Integer> e : productos.entrySet()) {
                                int pid = e.getKey();
                                int qty = e.getValue();
                                Producto p = null;
                                try { p = productoDao.leer(pid); } catch (Throwable ex) { p = null; }
                                double precioUnit = 0.0;
                                if (p != null) { try { precioUnit = p.getPrecio(); } catch (Throwable ignore) {} }
                                double sub = precioUnit * qty;

                                int idx = 1;
                                for (String col : insertCols) {
                                    String low = col.toLowerCase();
                                    if (low.equalsIgnoreCase(idVentaCol.toLowerCase())) { psProd.setLong(idx++, idVenta); continue; }
                                    if (low.equalsIgnoreCase(vpIdProd.toLowerCase())) { psProd.setInt(idx++, pid); continue; }
                                    if (vpQty != null && low.equalsIgnoreCase(vpQty.toLowerCase())) { psProd.setInt(idx++, qty); continue; }
                                    if (vpSubtotal != null && low.equalsIgnoreCase(vpSubtotal.toLowerCase())) { psProd.setDouble(idx++, sub); continue; }
                                    if (vpTipoCol != null && low.equalsIgnoreCase(vpTipoCol.toLowerCase())) { psProd.setInt(idx++, 1); continue; }
                                    // fallback: si columna existe y es NOT NULL sin default ponemos algo razonable
                                    ColumnMeta cm = metaByName.get(low);
                                    if (cm != null) {
                                        boolean isNotNull = cm.isNullable != null && cm.isNullable.equalsIgnoreCase("NO");
                                        boolean hasDefault = cm.defaultValue != null && !cm.defaultValue.trim().isEmpty();
                                        if (isNotNull && !hasDefault) {
                                            if (cm.dataType == Types.TIMESTAMP || cm.dataType == Types.DATE) psProd.setTimestamp(idx++, new Timestamp(System.currentTimeMillis()));
                                            else if (cm.dataType == Types.INTEGER || cm.dataType == Types.BIGINT) psProd.setInt(idx++, 0);
                                            else if (cm.dataType == Types.DECIMAL || cm.dataType == Types.DOUBLE || cm.dataType == Types.FLOAT) psProd.setDouble(idx++, 0.0);
                                            else psProd.setString(idx++, "");
                                        } else {
                                            psProd.setNull(idx++, cm.dataType);
                                        }
                                    } else {
                                        psProd.setNull(idx++, Types.NULL);
                                    }
                                }
                                psProd.addBatch();
                            }
                            psProd.executeBatch();
                        }
                    }
                }
            }

            // ------------------- 5) actualizar función asientos_disponibles -------------------
            String funcPk = detectColumn(con, "funciones", new String[] {"id_funcion","id_func","idFuncion"});
            if (funcPk == null) throw new SQLException("No se encontró PK de función.");
            try (PreparedStatement psUpd = con.prepareStatement("UPDATE funciones SET asientos_disponibles = GREATEST(0, asientos_disponibles - ?) WHERE " + funcPk + " = ?")) {
                psUpd.setInt(1, butacas.size());
                psUpd.setInt(2, rt.getIdFuncion());
                psUpd.executeUpdate();
            }

            con.commit();

            // limpiar sesión
            session.removeAttribute("reservaTemporal");
            session.removeAttribute("butacasSeleccionadas");
            session.removeAttribute("productosSeleccionados");
            session.removeAttribute("metodoPago");
            session.removeAttribute("clienteDatos");
            session.removeAttribute("totalEntradas");
            session.removeAttribute("carritoDulceria");

            resp.sendRedirect(req.getContextPath() + "/ClienteServlet?action=comprobante&idVenta=" + idVenta);

        } catch (Exception ex) {
            if (con != null) try { con.rollback(); } catch (SQLException ignored) {}
            throw ex;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ignored) {}
        }
    }

    /* ------------------ comprobante ------------------ */
    /* ------------------ comprobante (robusto) ------------------ */
private void accionComprobante(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    String sIdVenta = req.getParameter("idVenta");
    if (sIdVenta == null || sIdVenta.trim().isEmpty()) {
        resp.sendRedirect(req.getContextPath() + "/ClienteServlet?action=lista");
        return;
    }

    long idVenta;
    try { idVenta = Long.parseLong(sIdVenta); } catch (NumberFormatException ex) {
        resp.sendRedirect(req.getContextPath() + "/ClienteServlet?action=lista");
        return;
    }

    Connection con = null;
    try {
        con = Conexion.getConnection();

        // ---------- 1) Leer la fila de ventas (select * para mayor flexibilidad) ----------
        String sqlVenta = "SELECT * FROM ventas WHERE id_venta = ?";
        Long idUsuarioCliente = null;
        java.util.Date fechaVenta = null;
        Double totalVenta = 0.0;
        String metodoPago = null;

        try (PreparedStatement ps = con.prepareStatement(sqlVenta)) {
            ps.setLong(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Venta no encontrada: id=" + idVenta);

                // detectar columna de usuario en ventas
                String[] userCandidates = new String[] {
                    "id_usuario","id_cliente","id_usuario_cliente","usuario_id","id_user","idUsuario","id_usuario_cliente"
                };
                String userCol = detectColumn(con, "ventas", userCandidates);
                if (userCol == null) {
                    // fallback: buscar cualquier columna con "usuario" o "cliente"
                    List<ColumnMeta> vcols = getColumnsForTable(con, "ventas");
                    for (ColumnMeta cm : vcols) {
                        String low = cm.name.toLowerCase();
                        if (low.contains("usuario") || low.contains("cliente")) { userCol = cm.name; break; }
                    }
                }

                if (userCol != null && rs.getObject(userCol) != null) {
                    idUsuarioCliente = rs.getObject(userCol) instanceof Number ? rs.getLong(userCol) : null;
                }

                // fecha columna
                String fechaCol = detectColumn(con, "ventas", new String[] {"fecha","fecha_venta","created_at","fecha_hora","date"});
                if (fechaCol == null) fechaCol = "fecha";
                Timestamp ts = null;
                try { ts = rs.getTimestamp(fechaCol); } catch (Throwable ignore) {}
                if (ts != null) fechaVenta = new java.util.Date(ts.getTime());

                // total columna
                String totalCol = detectColumn(con, "ventas", new String[] {"total","importe","monto","valor"});
                if (totalCol == null) totalCol = "total";
                try { Object tv = rs.getObject(totalCol); if (tv != null) totalVenta = ((Number)tv).doubleValue(); } catch (Throwable ignore) {}

                // metodo pago
                String metodoCol = detectColumn(con, "ventas", new String[] {"metodo_pago","metodo","forma_pago","payment_method"});
                if (metodoCol == null) metodoCol = "metodo_pago";
                try { metodoPago = rs.getString(metodoCol); } catch (Throwable ignore) {}
            }
        }

        // ---------- 2) Nombre del cliente ----------
        String nombreCliente = "";
        if (idUsuarioCliente != null) {
            // intentar detectar la tabla de usuarios (por defecto 'usuarios')
            String usuariosTable = "usuarios";
            // detectar columna PK / id en usuarios
            String userIdCol = null;
            try {
                DatabaseMetaData md = con.getMetaData();
                try (ResultSet pk = md.getPrimaryKeys(null, null, usuariosTable)) {
                    if (pk.next()) userIdCol = pk.getString("COLUMN_NAME");
                }
            } catch (Throwable ignored) {}
            if (userIdCol == null) userIdCol = detectColumn(con, usuariosTable, new String[] {"id_usuario","id","id_user","usuario_id"});

            // detectar columna de nombre
            String nameCol = detectColumn(con, usuariosTable, new String[] {"nombre","nombres","nombre_completo","full_name","fullname","nombreUsuario"});
            if (nameCol == null) {
                // fallback: tomar primera columna textual que contenga 'nombre'
                List<ColumnMeta> ucols = getColumnsForTable(con, usuariosTable);
                for (ColumnMeta cm : ucols) {
                    if (cm.name.toLowerCase().contains("nombre")) { nameCol = cm.name; break; }
                }
            }
            if (nameCol == null) nameCol = "nombre"; // último recurso

            if (userIdCol != null) {
                String sqlUser = "SELECT " + nameCol + " FROM " + usuariosTable + " WHERE " + userIdCol + " = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlUser)) {
                    ps.setLong(1, idUsuarioCliente);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) nombreCliente = rs.getString(1);
                        if (nombreCliente == null) nombreCliente = "";
                    }
                } catch (Throwable ex) { nombreCliente = ""; }
            }
        }

        // ---------- 3) Localizar la tabla detalle y columnas (robusto) ----------
        String detailTable = findTableHavingColumns(con, new String[] {"id_venta","id_asiento"});
        if (detailTable == null) detailTable = findTableHavingColumns(con, new String[] {"id_venta","id_producto"});
        if (detailTable == null) {
            // fallback a 'detalle_ventas' si existe (tu BD la usa)
            List<ColumnMeta> tryCols = getColumnsForTable(con, "detalle_ventas");
            if (!tryCols.isEmpty()) detailTable = "detalle_ventas";
        }
        if (detailTable == null) throw new SQLException("No se encontró tabla de detalle de ventas.");

        String idAsientoCol = detectColumn(con, detailTable, new String[] {"id_asiento","idAsiento","asiento_id","codigo_asiento","asiento"});
        String idProdCol = detectColumn(con, detailTable, new String[] {"id_producto","idProducto","producto_id","id_prod"});
        String tipoCol = detectColumn(con, detailTable, new String[] {"tipo_item","tipo","tipoitem","item_tipo"});
        String qtyCol = detectColumn(con, detailTable, new String[] {"cantidad","qty","cantidad_producto","cantidad_prod","cantidad_vendida"});
        String subtotalCol = detectColumn(con, detailTable, new String[] {"subtotal","subtotal_producto","importe","monto","valor","total_producto","total"});

        // ---------- 4) Obtener butacas (si hay columna id_asiento) ----------
        List<String> butacas = new ArrayList<>();
        if (idAsientoCol != null) {
            StringBuilder sbBut = new StringBuilder();
            sbBut.append("SELECT a.codigo FROM ").append(detailTable).append(" dv JOIN asientos a ON dv.").append(idAsientoCol)
                .append(" = a.id_asiento WHERE dv.id_venta = ?");
            // si existe tipoCol, preferir filtrar por tipo=2 (asiento) también (añade seguridad)
            if (tipoCol != null) sbBut.append(" AND (dv.").append(idAsientoCol).append(" IS NOT NULL OR dv.").append(tipoCol).append(" = 2)");
            try (PreparedStatement ps = con.prepareStatement(sbBut.toString())) {
                ps.setLong(1, idVenta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) butacas.add(rs.getString("codigo"));
                }
            }
        }

        // ---------- 5) Obtener productos ----------
        class ProdLine { String nombre; int cantidad; double precio; double subtotal; }
        List<ProdLine> productosList = new ArrayList<>();
        double totalProductos = 0.0;
        if (idProdCol != null) {
            String qtyColToUse = (qtyCol != null) ? qtyCol : "cantidad";
            StringBuilder sbProd = new StringBuilder();
            sbProd.append("SELECT p.nombre AS nombreProd, ");
            // detectar nombre del id en productos (normalmente id_producto)
            String prodIdColInProductos = detectColumn(con, "productos", new String[] {"id_producto","idProd","id"});
            if (prodIdColInProductos == null) prodIdColInProductos = "id_producto";
            sbProd.append("p.precio AS precioProd, dv.").append(qtyColToUse).append(" AS qty");
            if (subtotalCol != null) sbProd.append(", dv.").append(subtotalCol).append(" AS subtotal");
            sbProd.append(" FROM ").append(detailTable).append(" dv JOIN productos p ON dv.").append(idProdCol)
                  .append(" = p.").append(prodIdColInProductos)
                  .append(" WHERE dv.id_venta = ?");
            if (tipoCol != null) sbProd.append(" AND dv.").append(tipoCol).append(" = 1");

            try (PreparedStatement ps = con.prepareStatement(sbProd.toString())) {
                ps.setLong(1, idVenta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ProdLine pl = new ProdLine();
                        pl.nombre = rs.getString("nombreProd");
                        Object priceObj = null;
                        try { priceObj = rs.getObject("precioProd"); } catch (Throwable ignored) {}
                        pl.precio = priceObj != null ? ((Number) priceObj).doubleValue() : 0.0;

                        Object qtyObj = null;
                        try { qtyObj = rs.getObject("qty"); } catch (Throwable ignored) {}
                        pl.cantidad = qtyObj != null ? ((Number) qtyObj).intValue() : 0;

                        if (subtotalCol != null) {
                            Object subObj = null;
                            try { subObj = rs.getObject("subtotal"); } catch (Throwable ignored) {}
                            if (subObj != null) pl.subtotal = ((Number) subObj).doubleValue();
                            else pl.subtotal = pl.precio * pl.cantidad;
                        } else {
                            pl.subtotal = pl.precio * pl.cantidad;
                        }

                        totalProductos += pl.subtotal;
                        productosList.add(pl);
                    }
                }
            }
        }

        // ---------- 6) Construir map 'venta' que el JSP usa ----------
        Map<String, Object> venta = new HashMap<>();
        String fechaStr = "";
        if (fechaVenta != null) {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd 'de' MMMM 'de' yyyy - HH:mm", new java.util.Locale("es","ES"));
            fechaStr = df.format(fechaVenta);
        }
        venta.put("idVenta", idVenta);
        venta.put("fecha", fechaStr);
        venta.put("cliente", nombreCliente != null ? nombreCliente : "");
        venta.put("total", totalVenta);
        venta.put("totalProductos", totalProductos);
        venta.put("totalEntradas", totalVenta - totalProductos);
        venta.put("metodoPago", metodoPago != null ? metodoPago : "");
        venta.put("butacas", butacas);
        venta.put("productosList", productosList);

        // atributos sueltos
        req.setAttribute("idVenta", idVenta);
        req.setAttribute("fechaVenta", fechaVenta);
        req.setAttribute("clienteNombre", nombreCliente);
        req.setAttribute("productosList", productosList);
        req.setAttribute("butacas", butacas);
        req.setAttribute("totalProductos", totalProductos);
        req.setAttribute("totalVenta", totalVenta);

        // poner el mapa para la JSP
        req.setAttribute("venta", venta);

        // forward
        req.getRequestDispatcher("/Cliente/Comprobante.jsp").forward(req, resp);

    } catch (Exception ex) {
        throw ex;
    } finally {
        if (con != null) try { con.close(); } catch (SQLException ignore) {}
    }
}


    /* ------------------ utilities ------------------ */
    private String findColumnName(List<ColumnMeta> colsMeta, String[] candidates) {
        Map<String,String> lowerToOrig = new LinkedHashMap<>();
        for (ColumnMeta cm : colsMeta) lowerToOrig.put(cm.name.toLowerCase(), cm.name);
        for (String cand : candidates) {
            String low = cand.toLowerCase();
            if (lowerToOrig.containsKey(low)) return lowerToOrig.get(low);
        }
        for (ColumnMeta cm : colsMeta) {
            String low = cm.name.toLowerCase();
            for (String cand : candidates) {
                if (low.contains(cand.replace("_","").toLowerCase()) || cand.replace("_","").toLowerCase().contains(low)) {
                    return cm.name;
                }
            }
        }
        return null;
    }

    private static class ColumnMeta {
        String name; int dataType; String isNullable; String defaultValue;
        ColumnMeta(String n,int dt,String inull,String def){ name=n; dataType=dt; isNullable=inull; defaultValue=def; }
    }
}