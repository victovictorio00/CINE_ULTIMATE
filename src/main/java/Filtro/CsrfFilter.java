package Filtro;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class CsrfFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;
        HttpSession session = httpReq.getSession(true);

        // 🔒 Anti-Clickjacking
        httpRes.setHeader("X-Frame-Options", "DENY");
        httpRes.setHeader("X-Content-Type-Options", "nosniff");

        // 1. Crear token solo si NO existe
        String csrfToken = (String) session.getAttribute("csrfToken");
        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
        }

        // 2. Validar POST con token
        if ("POST".equalsIgnoreCase(httpReq.getMethod())) {
            String tokenRecibido = httpReq.getParameter("csrf_token");
            if (tokenRecibido == null || !tokenRecibido.equals(csrfToken)) {
                httpRes.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Token CSRF inválido o ausente.");
                return;
            }
        }

        // 3. Continuar ejecución
        chain.doFilter(req,res);
    }
}