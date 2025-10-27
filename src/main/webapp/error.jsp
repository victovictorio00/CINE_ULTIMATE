<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<%!
    // Pequeña utilidad para escapar HTML (suficiente para depuración)
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error - CineOnline</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 24px;
        }
        .error-container {
            background: white;
            padding: 2.5em;
            border-radius: 12px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.08);
            width: 100%;
            max-width: 980px;
        }
        h1 { color: #dc3545; margin-bottom: 0.25em; font-weight:700; }
        p { color: #6c757d; font-size:1rem; }
        pre {
            background: #0b1220;
            color: #dff3c8;
            padding: 14px;
            border-radius: 8px;
            overflow: auto;
            max-height: 380px;
            white-space: pre-wrap;
            word-break: break-word;
        }
        .muted { color:#6c757d; }
        .btn-home { margin-right: 8px; }
        .trace-toggle { margin-top: 12px; }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>¡Ups! Algo salió mal</h1>
        <p class="muted">Ocurrió un error inesperado. Por favor, intenta nuevamente más tarde.</p>

        <%
            // Preferimos el atributo "stacktrace" que el servlet puede poner en request
            String stack = (String) request.getAttribute("stacktrace");
            String errMsg = (String) request.getAttribute("error");

            // Si no hay stack en request, intentar la excepción estándar
            if ((stack == null || stack.trim().isEmpty())) {
                Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
                if (t != null) {
                    java.io.StringWriter sw = new java.io.StringWriter();
                    t.printStackTrace(new java.io.PrintWriter(sw));
                    stack = sw.toString();
                }
            }

            // Si tampoco hay error simple, intentar el mensaje de excepción
            if ((errMsg == null || errMsg.trim().isEmpty())) {
                if (stack != null && !stack.isEmpty()) {
                    // intentar extraer primera linea
                    int nl = stack.indexOf('\n');
                    if (nl > 0) errMsg = stack.substring(0, nl);
                    else errMsg = "Error (ver traza completa)";
                } else {
                    errMsg = "Ocurrió un error inesperado.";
                }
            }
        %>

        <p style="font-weight:600;"><%= escapeHtml(errMsg) %></p>

        <% if (stack != null && !stack.trim().isEmpty()) { %>
            <div style="margin-top:16px;">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <strong>Traza de depuración</strong>
                    <div>
                        <button class="btn btn-sm btn-outline-secondary btn-home" id="btnCopy">Copiar traza</button>
                        <a class="btn btn-sm btn-primary btn-home" href="<%= request.getContextPath()%>/">Volver al inicio</a>
                    </div>
                </div>
                <pre id="tracePre"><%= escapeHtml(stack) %></pre>
            </div>

            <script>
                // Copiar traza al portapapeles
                document.getElementById('btnCopy').addEventListener('click', function(){
                    const text = document.getElementById('tracePre').innerText;
                    if (navigator.clipboard) {
                        navigator.clipboard.writeText(text).then(function(){ 
                            alert('Traza copiada al portapapeles.');
                        }, function(){ alert('No se pudo copiar la traza.'); });
                    } else {
                        // Fallback
                        const ta = document.createElement('textarea');
                        ta.value = text;
                        document.body.appendChild(ta);
                        ta.select();
                        try { document.execCommand('copy'); alert('Traza copiada al portapapeles.'); } catch(e) { alert('No se pudo copiar la traza.'); }
                        ta.remove();
                    }
                });
            </script>
        <% } else { %>
            <div style="margin-top:16px;">
                <a class="btn btn-primary btn-home" href="<%= request.getContextPath()%>/">Volver al inicio</a>
            </div>
        <% } %>
    </div>
</body>
</html>
