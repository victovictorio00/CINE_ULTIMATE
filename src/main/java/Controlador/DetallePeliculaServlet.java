/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Pelicula;
import modelo.PeliculaDao;

/**
 *
 * @author Desktop
 */
@WebServlet(name = "DetallePeliculaServlet", urlPatterns = {"/DetallePeliculaServlet"})
public class DetallePeliculaServlet extends HttpServlet {

    private PeliculaDao peliculaDao;

    @Override
    public void init() throws ServletException {
        this.peliculaDao = new PeliculaDao();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- INICIO DE VALIDACIÓN DE SESIÓN ---
        // 1. Obtener la sesión existente (sin crear una nueva)
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        // 2. Comprobar si el usuario NO está logueado
        if (username == null || username.isEmpty()) {

            // Opcional: Construir la URL de login con parámetro de redirección
            // Esto permite que el LoginServlet sepa a dónde enviar al usuario después de loguearse.
            String requestedId = request.getParameter("id");
            String loginUrl = request.getContextPath() + "/Login.jsp";

            if (requestedId != null && !requestedId.isEmpty()) {
                // Se asume que /Login.jsp tiene un mecanismo para leer 'redirect' y volver aquí.
                loginUrl += "?redirect=DetallePeliculaServlet&id=" + requestedId;
            }

            // Redirigir al usuario a la página de login
            response.sendRedirect(loginUrl);
            return; // ¡IMPORTANTE! Detener la ejecución del doGet aquí.
        }
        // --- FIN DE VALIDACIÓN DE SESIÓN ---

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el ID de la película");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Pelicula pelicula = peliculaDao.leer(id);

            if (pelicula == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Película no encontrada");
                return;
            }

            // Enviar la película al JSP
            request.setAttribute("pelicula", pelicula);
            request.getRequestDispatcher("/Cliente/DetallePelicula.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener la película");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
