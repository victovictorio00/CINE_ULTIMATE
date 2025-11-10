package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import modelo.Funcion;
import modelo.FuncionDao;
import modelo.Pelicula;
import modelo.PeliculaDao;
import modelo.Sala;
import modelo.SalaDao;
import modelo.EstadoFuncion;

@WebServlet("/FuncionServlet")
public class FuncionServlet extends HttpServlet {

    private FuncionDao funcionDao;
    private PeliculaDao peliculaDAO;
    private SalaDao salaDAO;

    @Override
    public void init() {
        funcionDao = new FuncionDao();
        peliculaDAO = new PeliculaDao();
        salaDAO = new SalaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
                case "editar":
                    editarFuncion(request, response);
                    break;
                default:
                    listarFunciones(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar la acción: " + action, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
            } else if ("actualizar".equals(action)) {
                actualizarFuncion(request, response);
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
        try {
            List<Funcion> lista = funcionDao.listar();
            request.setAttribute("listaFunciones", lista);
            request.setAttribute("listaPeliculas", peliculaDAO.listar());
            request.setAttribute("listaSalas", salaDAO.listar());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "⚠ Error al listar funciones");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("Funcion.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("listaPeliculas", peliculaDAO.listar());
            request.setAttribute("listaSalas", salaDAO.listar());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "⚠ Error al cargar listas para nueva función");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearFuncion.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarFuncion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int idPelicula = Integer.parseInt(request.getParameter("id_pelicula"));
        int idSala = Integer.parseInt(request.getParameter("id_sala"));
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        int idEstadoFuncion = Integer.parseInt(request.getParameter("id_estado_funcion"));
        int asientosDisponibles = Integer.parseInt(request.getParameter("asientos_disponibles"));

        Funcion f = new Funcion();
        Pelicula pelicula = new Pelicula(); pelicula.setIdPelicula(idPelicula); f.setPelicula(pelicula);
        Sala sala = new Sala(); sala.setIdSala(idSala); f.setSala(sala);
        EstadoFuncion estado = new EstadoFuncion(); estado.setIdEstadoFuncion(idEstadoFuncion); f.setEstadoFuncion(estado);

        Timestamp inicio = Timestamp.valueOf(fechaInicio.replace("T", " ") + ":00");
        Timestamp fin = Timestamp.valueOf(fechaFin.replace("T", " ") + ":00");
        f.setFechaInicio(inicio);
        f.setFechaFin(fin);
        f.setAsientosDisponibles(asientosDisponibles);

        // Validación de solapamiento de horarios
        if (funcionDao.existeConflicto(idSala, inicio, fin, null)) {
            request.setAttribute("mensaje", "⚠ Horario ocupado en esta sala.");
            listarFunciones(request, response);
            return;
        }

        funcionDao.insertar(f);
        response.sendRedirect("FuncionServlet?action=listar");
    }

    private void eliminarFuncion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        funcionDao.eliminar(id);
        response.sendRedirect("FuncionServlet?action=listar");
    }

    private void editarFuncion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Funcion f = funcionDao.obtener(id);

        request.setAttribute("funcion", f);
        request.setAttribute("listaPeliculas", peliculaDAO.listar());
        request.setAttribute("listaSalas", salaDAO.listar());

        RequestDispatcher dispatcher = request.getRequestDispatcher("EditarFuncion.jsp");
        dispatcher.forward(request, response);
    }

    private void actualizarFuncion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int idFuncion = Integer.parseInt(request.getParameter("id_funcion"));
        int idPelicula = Integer.parseInt(request.getParameter("id_pelicula"));
        int idSala = Integer.parseInt(request.getParameter("id_sala"));
        int idEstadoFuncion = Integer.parseInt(request.getParameter("id_estado_funcion"));
        int asientosDisponibles = Integer.parseInt(request.getParameter("asientos_disponibles"));

        Timestamp inicio = Timestamp.valueOf(request.getParameter("fecha_inicio").replace("T", " ") + ":00");
        Timestamp fin = Timestamp.valueOf(request.getParameter("fecha_fin").replace("T", " ") + ":00");

        if (funcionDao.existeConflicto(idSala, inicio, fin, idFuncion)) {
            request.setAttribute("mensaje", "⚠ Horario ocupado en esta sala.");
            listarFunciones(request, response);
            return;
        }

        Funcion f = new Funcion();
        f.setIdFuncion(idFuncion);

        Pelicula p = new Pelicula(); p.setIdPelicula(idPelicula); f.setPelicula(p);
        Sala s = new Sala(); s.setIdSala(idSala); f.setSala(s);
        EstadoFuncion e = new EstadoFuncion(); e.setIdEstadoFuncion(idEstadoFuncion); f.setEstadoFuncion(e);

        f.setFechaInicio(inicio);
        f.setFechaFin(fin);
        f.setAsientosDisponibles(asientosDisponibles);

        funcionDao.actualizar(f);
        response.sendRedirect("FuncionServlet?action=listar");
    }
}
