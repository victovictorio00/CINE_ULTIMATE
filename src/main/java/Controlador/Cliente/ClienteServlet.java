package Controlador.Cliente;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import modelo.Pelicula;
import modelo.PeliculaDao;
import modelo.Asiento;  // Tu clase Asiento real
import modelo.AsientoDao;  // Tu DAO de asientos
import modelo.Funcion;  // Necesitarás esta clase
import modelo.FuncionDao;  // Y su DAO
import java.util.List;
import java.util.ArrayList;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private PeliculaDao peliculaDao;
    private AsientoDao asientoDao;
    private FuncionDao funcionDao;

    @Override
    public void init() {
        peliculaDao = new PeliculaDao();
        asientoDao = new AsientoDao();
        funcionDao = new FuncionDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- INICIO DE VALIDACIÓN DE SESIÓN (Mejorada) ---
        HttpSession session = request.getSession(false);
        String username = null;
        if (session != null) {
            Object u = session.getAttribute("username");
            if (u instanceof String) {
                username = ((String) u).trim();
            }
        }

        if (username == null || username.isEmpty()) {
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String fullRedirectUrl = uri + (query != null && !query.isEmpty() ? "?" + query : "");

            String context = request.getContextPath();
            if (!fullRedirectUrl.startsWith(context)) {
                fullRedirectUrl = context + "/";
            }

            String encodedUrl = java.net.URLEncoder.encode(fullRedirectUrl, java.nio.charset.StandardCharsets.UTF_8.name());
            String loginUrl = response.encodeRedirectURL(context + "/Login.jsp?redirect=" + encodedUrl);

            response.sendRedirect(loginUrl);
            return;
        }
        // --- FIN DE VALIDACIÓN DE SESIÓN ---

        String action = request.getParameter("action");
        try {
            if ("listar".equals(action)) {
                listarPeliculas(request, response);
            } else if ("reservar".equals(action)) {
                mostrarSeleccionAsiento(request, response);
            } else if ("confirmarPago".equals(action)) {
                mostrarVoucher(request, response);
            } else if ("metodoPago".equals(action)) {
                mostrarMetodoPago(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar la solicitud", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("seleccionarCombo".equals(action)) {
                seleccionarCombo(request, response);
            } else if ("confirmarAsiento".equals(action)) {
                procesarSeleccionAsiento(request, response);
            } else if ("procesarPago".equals(action)) {
                procesarPago(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar la acción", e);
        }
    }

    private void procesarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idFuncion = request.getParameter("idFuncion");
        String asientoSeleccionado = request.getParameter("asientoSeleccionado");

        HttpSession session = request.getSession();
        session.setAttribute("idFuncion", idFuncion);
        session.setAttribute("asientoSeleccionado", asientoSeleccionado);

        request.setAttribute("idFuncion", idFuncion);
        request.setAttribute("asientoSeleccionado", asientoSeleccionado);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/MetodoPago.jsp");
        dispatcher.forward(request, response);
    }

    private void listarPeliculas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Pelicula> peliculas = peliculaDao.listar();
        request.setAttribute("peliculas", peliculas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/DashboardCliente.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * MÉTODO ACTUALIZADO: Muestra la selección de asientos con datos reales de la BD
     */
    private void mostrarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        // IMPORTANTE: Ahora necesitas recibir el ID de la FUNCIÓN, no solo de la película
        String idFuncionParam = request.getParameter("idFuncion");
        
        if (idFuncionParam == null || idFuncionParam.isEmpty()) {
            // Si no viene idFuncion, redirigir a la página de selección de función
            response.sendRedirect(request.getContextPath() + "/Cliente/SeleccionFuncion.jsp");
            return;
        }
        
        int idFuncion = Integer.parseInt(idFuncionParam);
        
        // Obtener información de la función
        Funcion funcion = funcionDao.leer(idFuncion);
        
        if (funcion == null) {
            request.setAttribute("error", "Función no encontrada");
            RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/Error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Obtener información de la película
        Pelicula pelicula = peliculaDao.leer(funcion.getPelicula().getIdPelicula());
        
        // Obtener asientos con su estado actual para esta función
        List<Asiento> asientos = asientoDao.obtenerAsientosPorSalaYFuncion(
            funcion.getSala().getIdSala(), 
            idFuncion
        );
        
        // Pasar datos al JSP
        request.setAttribute("asientos", asientos);
        request.setAttribute("pelicula", pelicula);
        request.setAttribute("funcion", funcion);
        request.setAttribute("idFuncion", idFuncion);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/SeleccionAsiento.jsp");
        dispatcher.forward(request, response);
    }

    private void seleccionarCombo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        String selectedSeats = request.getParameter("selectedSeats");
        String idFuncion = request.getParameter("idFuncion");
        
        // Guardar en sesión
        HttpSession session = request.getSession();
        session.setAttribute("selectedSeats", selectedSeats);
        session.setAttribute("idFuncion", idFuncion);
        
        // Validar que los asientos estén disponibles antes de continuar
        // (Opcional pero recomendado)
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/SeleccionarCombo.jsp");
        dispatcher.forward(request, response);
    }

    protected void procesarPago(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");

        // Aquí iría la lógica para validar y procesar el pago
        System.out.println("Procesando pago con tarjeta: " + cardNumber);

        response.sendRedirect(request.getContextPath() + "/Cliente/Confirmacion.jsp");
    }

    private void mostrarVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/Voucher.jsp");
        dispatcher.forward(request, response);
    }

    private void generarVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String asientoSeleccionado = request.getParameter("asiento");
        String comboSeleccionado = request.getParameter("combo");
        String metodoPago = request.getParameter("metodoPago");

        double precioAsiento = 10.00;
        double precioCombo = 15.00;
        double total = precioAsiento + precioCombo;

        request.setAttribute("asiento", asientoSeleccionado);
        request.setAttribute("combo", comboSeleccionado);
        request.setAttribute("metodoPago", metodoPago);
        request.setAttribute("total", total);

        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/Voucher.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarMetodoPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/MetodoPago.jsp");
        dispatcher.forward(request, response);
    }
}