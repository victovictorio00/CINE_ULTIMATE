package Controlador; // ajusta
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.PeliculaDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ImageServlet.class.getName());
    private PeliculaDao peliculaDao;

    @Override
    public void init() throws ServletException {
        this.peliculaDao = new PeliculaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de película requerido.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
            return;
        }

        try {
            byte[] imagen = peliculaDao.obtenerFotoPorId(id);

            if (imagen == null || imagen.length == 0) {
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("No image for id " + id);
                return;
            }

            // Detectar MIME (solo JPEG o PNG)
            String mime = detectMimeJpegPng(imagen);
            if (mime == null) {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Only JPEG/PNG images are supported.");
                return;
            }

            // Cabeceras antes de escribir
            response.setContentType(mime);
            response.setContentLengthLong(imagen.length);
            response.setHeader("Cache-Control", "public, max-age=86400");

            // Escribir los bytes
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(imagen);
                out.flush();
            }

        } catch (SQLException e) {
            logger.severe("Error al obtener imagen de BD: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de base de datos.");
        }
    }

    private String detectMimeJpegPng(byte[] b) {
        if (b == null || b.length < 4) {
            return null;
        }
        // JPEG: FF D8 FF
        if ((b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }
        // PNG: 89 50 4E 47
        if (b.length >= 8 && (b[0] & 0xFF) == 0x89 && b[1] == 0x50 && b[2] == 0x4E && b[3] == 0x47) {
            return "image/png";
        }
        return null;
    }
}