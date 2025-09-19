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
        //hola
        // Control de acceso manual: Solo admin puede acceder
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("listar".equals(action)) {
                listarEmpleados(request, response);
            } else if ("nuevo".equals(action)) {
                mostrarFormularioNuevo(request, response);
            } else if ("editar".equals(action)) {
                mostrarFormularioEditar(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarEmpleado(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Control de acceso manual: Solo admin puede acceder
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("insertar".equals(action)) {
                insertarEmpleado(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarEmpleado(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Empleado> lista = empleadoDao.listar();  // Esto debería devolver la lista de empleados desde la base de datos
        if (lista != null && !lista.isEmpty()) {
            request.setAttribute("listaEmpleados", lista);
            // Asegúrate de que los empleados se están pasando correctamente al JSP
        } else {
            request.setAttribute("mensaje", "No hay empleados disponibles.");  // Agregar mensaje cuando no hay empleados
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");  // Verifica que 'index.jsp' sea el nombre correcto
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
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Empleado emp = empleadoDao.leer(id);
                if (emp != null) {
                    request.setAttribute("empleado", emp);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("EditarEmpleado.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.getWriter().println("Empleado no encontrado");
                }
            } catch (NumberFormatException e) {
                response.getWriter().println("ID inválido, no se puede convertir a número.");
            }
        } else {
            response.getWriter().println("ID no proporcionado.");
        }
    }

    private void insertarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String cargo = request.getParameter("cargo");
        String salarioStr = request.getParameter("salario");
        Double salario = Double.parseDouble(salarioStr);

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
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String cargo = request.getParameter("cargo");
        String salarioStr = request.getParameter("salario");
        Double salario = Double.parseDouble(salarioStr);

        Empleado emp = new Empleado(id, nombre, direccion, telefono, cargo, salario);
        empleadoDao.editar(emp);
        response.sendRedirect("EmpleadoServlet?action=listar");
    }

    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        empleadoDao.eliminar(id);
        response.sendRedirect("EmpleadoServlet?action=listar");
    }
}
