<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Error</title></head>
<body>
    <h1>Ha ocurrido un error</h1>
    <p><strong>Mensaje:</strong> <%= request.getAttribute("error") %></p>
    <a href="<%= request.getContextPath() %>/ClienteServlet?action=listar">Volver al inicio</a>
</body>
</html>