package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Funcion;
import modelo.FuncionDao;

@WebServlet("/FuncionServlet")
public class FuncionServlet extends HttpServlet {

    private FuncionDao funcionDao;

    @Override
    public void init() {
        funcionDao = new FuncionDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Control de sesión: solo administradores
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null ||
            !"admin".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "listar":
                    listarFunciones(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "eliminar":
                    eliminarFuncion(request, response);
                    break;
                default:
                    listarFunciones(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 🔍 útil para depurar
            throw new ServletException("Error al procesar la acción: " + action, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Control de sesión: solo administradores
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null ||
            !"admin".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("insertar".equals(action)) {
                insertarFuncion(request, response);
            } else {
                listarFunciones(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar POST en FuncionServlet", e);
        }
    }

    // ==============================
    // MÉTODOS CRUD
    // ==============================

    private void listarFunciones(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Funcion> lista = funcionDao.listar();
        request.setAttribute("listaFunciones", lista);

        RequestDispatcher dispatcher = request.getRequestDispatcher("Funcion.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // En el futuro puedes cargar combos de películas, salas y estados
        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearFuncion.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarFuncion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int idPelicula = Integer.parseInt(request.getParameter("id_pelicula"));
        int idSala = Integer.parseInt(request.getParameter("id_sala"));
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        int idEstadoFuncion = Integer.parseInt(request.getParameter("id_estado_funcion"));
        int asientosDisponibles = Integer.parseInt(request.getParameter("asientos_disponibles"));

        Funcion f = new Funcion();

        // --- Película ---
        modelo.Pelicula pelicula = new modelo.Pelicula();
        pelicula.setIdPelicula(idPelicula);
        f.setPelicula(pelicula);

        // --- Sala ---
        modelo.Sala sala = new modelo.Sala();
        sala.setIdSala(idSala);
        f.setSala(sala);

        // --- Fechas ---
        try {
            f.setFechaInicio(java.sql.Timestamp.valueOf(fechaInicio.replace("T", " ") + ":00"));
            f.setFechaFin(java.sql.Timestamp.valueOf(fechaFin.replace("T", " ") + ":00"));
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Error al convertir fechas: " + e.getMessage());
        }

        // --- Estado ---
        modelo.EstadoFuncion estado = new modelo.EstadoFuncion();
        estado.setIdEstadoFuncion(idEstadoFuncion);
        f.setEstadoFuncion(estado);

        // --- Asientos ---
        f.setAsientosDisponibles(asientosDisponibles);

        funcionDao.insertar(f);
        response.sendRedirect("FuncionServlet?action=listar");
    }

    private void eliminarFuncion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        funcionDao.eliminar(id);
        response.sendRedirect("FuncionServlet?action=listar");
    }
}
