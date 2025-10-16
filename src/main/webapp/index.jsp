<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="modelo.Empleado" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Lista de Empleados</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    </head>
    <body>

        <div class="container mt-5">
            <h3>Empleados</h3>
            <a href="EmpleadoServlet?action=nuevo" class="btn btn-success mb-3">Agregar Empleado</a>

            <!-- Aquí añadimos el código para depurar -->
            <%
                // Obtén la lista de empleados del request
                List<Empleado> lista = (List<Empleado>) request.getAttribute("listaEmpleados");
                
                // Verifica si la lista está vacía
                if (lista != null && lista.size() > 0) {
                    out.println("<table class='table table-striped'>");
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>ID</th>");
                    out.println("<th>Nombre</th>");
                    out.println("<th>Dirección</th>");
                    out.println("<th>Teléfono</th>");
                    out.println("<th>Acciones</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    
                    // Itera a través de la lista y muestra los empleados
                    for (Empleado empleado : lista) {
                        out.println("<tr>");
                        out.println("<td>" + empleado.getIdEmpleado() + "</td>");
                        out.println("<td>" + empleado.getNombre() + "</td>");
                        out.println("<td>" + empleado.getDireccion() + "</td>");
                        out.println("<td>" + empleado.getTelefono() + "</td>");
                        out.println("<td>");
                        out.println("<a href='EmpleadoServlet?action=editar&id=" + empleado.getIdEmpleado() + "' class='btn btn-primary btn-sm'>Editar</a>");
                        out.println("<a href='EmpleadoServlet?action=eliminar&id=" + empleado.getIdEmpleado() + "' class='btn btn-danger btn-sm' onclick='return confirm(\"¿Está seguro de eliminar este empleado?\");'>Eliminar</a>");
                        out.println("</td>");
                        out.println("</tr>");
                    }
                    
                    out.println("</tbody>");
                    out.println("</table>");
                } else {
                    out.println("No hay empleados disponibles.");
                }
            %>
        </div>

    </body>
</html>
