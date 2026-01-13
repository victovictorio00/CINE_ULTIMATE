package Filtro;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("*.jsp")
public class FiltroAccesibilidad implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        // Continuar con la ejecución normal
        chain.doFilter(req, res);

        //DESPUÉS de la JSP
        RequestDispatcher rd = httpReq.getRequestDispatcher("/Cliente/accesibilidad/accesibilidad.jsp");
        rd.include(req, res);
    }
}
