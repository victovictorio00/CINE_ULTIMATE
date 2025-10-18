package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Empleado;
import modelo.EmpleadoDao;

@WebServlet("/EmpleadoServlet")
public class EmpleadoServlet extends HttpServlet {

    private EmpleadoDao empleadoDao;

    @Override
    public void init() {
        empleadoDao = new EmpleadoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Control de acceso: solo administradores
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null ||
            !"admin".equals(session.getAttribute("rol"))) {
            // 🚪 Si no hay sesión o no es admin → redirige al Login.jsp
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "listar":
                    listarEmpleados(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "eliminar":
                    eliminarEmpleado(request, response);
                    break;
                default:
                    listarEmpleados(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar la acción: " + action, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Control de acceso: Solo administradores pueden acceder
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insertar":
                    insertarEmpleado(request, response);
                    break;
                case "actualizar":
                    actualizarEmpleado(request, response);
                    break;
                default:
                    listarEmpleados(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar el formulario de empleado.", e);
        }
    }

    // ==============================
    // MÉTODOS CRUD
    // ==============================

    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Empleado> lista = empleadoDao.listar();
        request.setAttribute("listaEmpleados", lista);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Empleado.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearEmpleado.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.getWriter().println("ID no proporcionado.");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Empleado emp = empleadoDao.leer(id);

            if (emp != null) {
                request.setAttribute("empleado", emp);
                RequestDispatcher dispatcher = request.getRequestDispatcher("EditarEmpleado.jsp");
                dispatcher.forward(request, response);
            } else {
                response.getWriter().println("Empleado no encontrado.");
            }
        } catch (NumberFormatException e) {
            response.getWriter().println("ID inválido, no se puede convertir a número.");
        }
    }

    private void insertarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String cargo = request.getParameter("cargo");
        String salarioStr = request.getParameter("salario");

        double salario = 0.0;
        if (salarioStr != null && !salarioStr.isEmpty()) {
            salario = Double.parseDouble(salarioStr);
        }

        Empleado emp = new Empleado();
        emp.setNombre(nombre);
        emp.setDireccion(direccion);
        emp.setTelefono(telefono);
        emp.setCargo(cargo);
        emp.setSalario(salario);

        empleadoDao.insertar(emp);
        response.sendRedirect("EmpleadoServlet?action=listar");
    }

    private void actualizarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            String cargo = request.getParameter("cargo");
            String salarioStr = request.getParameter("salario");

            double salario = 0.0;
            if (salarioStr != null && !salarioStr.isEmpty()) {
                salario = Double.parseDouble(salarioStr);
            }

            Empleado emp = new Empleado();
            emp.setIdEmpleado(id);
            emp.setNombre(nombre);
            emp.setDireccion(direccion);
            emp.setTelefono(telefono);
            emp.setCargo(cargo);
            emp.setSalario(salario);

            empleadoDao.editar(emp);
            response.sendRedirect("EmpleadoServlet?action=listar");

        } catch (NumberFormatException e) {
            response.getWriter().println("Error: ID o salario inválido.");
        }
    }

    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        empleadoDao.eliminar(id);
        response.sendRedirect("EmpleadoServlet?action=listar");
    }
}
