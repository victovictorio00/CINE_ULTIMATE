/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class CspFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Content-Security-Policy",
        "default-src 'self'; " +
        "script-src 'self' https://www.google.com https://www.gstatic.com https://www.gstatic.com/recaptcha/ https://www.recaptcha.net 'unsafe-inline'; " +
        "style-src 'self' 'unsafe-inline' https://stackpath.bootstrapcdn.com https://fonts.googleapis.com; " +
        "img-src 'self' data: https:; " +
        "font-src 'self' https://fonts.gstatic.com; " +
        "frame-src https://www.google.com https://www.gstatic.com https://www.recaptcha.net; " +
        "connect-src 'self' https://www.google.com https://www.gstatic.com https://www.recaptcha.net; " +
        "form-action 'self'; " +
        "object-src 'none'; " +
        "frame-ancestors 'none';");
        res.setHeader("X-Frame-Options", "DENY");
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("X-XSS-Protection", "1; mode=block");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
