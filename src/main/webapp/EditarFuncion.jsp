<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Funcion, modelo.Pelicula, modelo.Sala" %>

<%
    Funcion f = (Funcion) request.getAttribute("funcion");
    List<Pelicula> listaPeliculas = (List<Pelicula>) request.getAttribute("listaPeliculas");
    List<Sala> listaSalas = (List<Sala>) request.getAttribute("listaSalas");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Función</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
</head>
<body class="container mt-4">

    <h3>Editar Función</h3>
    <form action="FuncionServlet?action=actualizar" method="post">

        <input type="hidden" name="id_funcion" value="<%= f.getIdFuncion() %>">

        <div class="form-group">
            <label>Película</label>
            <select name="id_pelicula" class="form-control">
                <% for (Pelicula p : listaPeliculas) { %>
                    <option value="<%= p.getIdPelicula() %>" <%= (f.getPelicula().getIdPelicula() == p.getIdPelicula()) ? "selected" : "" %>>
                        <%= p.getNombre() %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label>Sala</label>
            <select name="id_sala" class="form-control">
                <% for (Sala s : listaSalas) { %>
                    <option value="<%= s.getIdSala() %>" <%= (f.getSala().getIdSala() == s.getIdSala()) ? "selected" : "" %>>
                        <%= s.getNombre() %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label>Fecha Inicio</label>
            <input type="datetime-local" name="fecha_inicio" class="form-control"
                   value="<%= f.getFechaInicio().toString().replace(' ', 'T') %>">
        </div>

        <div class="form-group">
            <label>Fecha Fin</label>
            <input type="datetime-local" name="fecha_fin" class="form-control"
                   value="<%= f.getFechaFin().toString().replace(' ', 'T') %>">
        </div>

        <div class="form-group">
            <label>Estado</label>
            <select name="id_estado_funcion" class="form-control">
                <option value="1" <%= f.getEstadoFuncion().getIdEstadoFuncion() == 1 ? "selected" : "" %>>Disponible</option>
                <option value="2" <%= f.getEstadoFuncion().getIdEstadoFuncion() == 2 ? "selected" : "" %>>En curso</option>
                <option value="3" <%= f.getEstadoFuncion().getIdEstadoFuncion() == 3 ? "selected" : "" %>>Finalizada</option>
            </select>
        </div>

        <div class="form-group">
            <label>Asientos Disponibles</label>
            <input type="number" name="asientos_disponibles" class="form-control" value="<%= f.getAsientosDisponibles() %>">
        </div>

        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="FuncionServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</body>
</html>
