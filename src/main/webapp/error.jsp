<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            height: 100vh;
            flex-direction: column;
            text-align: center;
        }
        .error-container {
            background: white;
            padding: 3em 4em;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        h1 {
            color: #dc3545;
            font-weight: bold;
            margin-bottom: 0.5em;
        }
        p {
            color: #6c757d;
            font-size: 1.2em;
        }
        .btn-home {
            margin-top: 1.5em;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>¡Ups! Algo salió mal</h1>
        <p>Ocurrió un error inesperado. Por favor, intenta nuevamente más tarde.</p>
        <a href="index.jsp" class="btn btn-primary btn-home">Volver al inicio</a>
    </div>
</body>
</html>
