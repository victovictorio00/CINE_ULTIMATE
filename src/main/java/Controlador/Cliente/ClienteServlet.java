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
import modelo.Cliente.Pelicula;
import modelo.Cliente.PeliculaDaoCliente;
import modelo.Cliente.Asiento;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    private PeliculaDao peliculaDao;

    @Override
    public void init() {
        peliculaDao = new PeliculaDao();
    }

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    try {
        if ("listar".equals(action)) {
            listarPeliculas(request, response);
        } else if ("reservar".equals(action)) {
            mostrarSeleccionAsiento(request, response);
        } else if ("confirmarPago".equals(action)) {
            mostrarVoucher(request, response);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("error", "Error en base de datos: " + e.getMessage());
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String action = request.getParameter("action");

    if ("seleccionarCombo".equals(action)) {
        seleccionarCombo(request, response);
    } else if ("confirmarAsiento".equals(action)) {
        procesarSeleccionAsiento(request, response);
    } else if ("procesarPago".equals(action)) {
        procesarPago(request, response);
    }
}
private void procesarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String idPelicula = request.getParameter("idPelicula");
    String asientoSeleccionado = request.getParameter("asientoSeleccionado");

    // Aquí puedes guardar en sesión si quieres simular estado
    HttpSession session = request.getSession();
    session.setAttribute("idPelicula", idPelicula);
    session.setAttribute("asientoSeleccionado", asientoSeleccionado);

    // Por ejemplo, redirige a un JSP de pago o voucher
    request.setAttribute("idPelicula", idPelicula);
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

    private void mostrarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        int idPelicula = Integer.parseInt(request.getParameter("id"));
        Pelicula pelicula = peliculaDao.leer(idPelicula);
        request.setAttribute("pelicula", pelicula);

        // Simulación lista de asientos
        List<Asiento> asientos = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            asientos.add(new Asiento(i, true)); 
        }
        request.setAttribute("asientos", asientos);
        request.setAttribute("idPelicula", idPelicula);

        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/SeleccionAsiento.jsp");
        dispatcher.forward(request, response);
    }


private void seleccionarCombo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String selectedSeats = request.getParameter("selectedSeats");
    // Guardar butacas seleccionadas en sesión para uso futuro
    request.getSession().setAttribute("selectedSeats", selectedSeats);

    // Redirigir a la página de selección de combos
    RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/SeleccionarCombo.jsp");
    dispatcher.forward(request, response);
}

    
    
    
    
    
    
  protected void procesarPago(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Obtener datos de la tarjeta (simulación)
    String cardNumber = request.getParameter("cardNumber");
    String expiryDate = request.getParameter("expiryDate");
    String cvv = request.getParameter("cvv");

    // Aquí iría la lógica para validar y procesar el pago, por ahora simulado
    System.out.println("Procesando pago con tarjeta: " + cardNumber);

    // Después de procesar, redirigir a página de confirmación o resumen
    response.sendRedirect(request.getContextPath() + "/Cliente/Confirmacion.jsp");
}
private void mostrarVoucher(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // No hay conexión DB, puedes pasar datos ficticios o datos de sesión si quieres
    // Ejemplo: request.setAttribute("numeroVoucher", "ABC12345");

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
    
    
}
