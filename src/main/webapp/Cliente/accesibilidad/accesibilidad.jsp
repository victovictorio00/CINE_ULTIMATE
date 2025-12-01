<%-- /Cliente/Componentes/accesibilidad.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="Cliente/accesibilidad/accesibilidad.css">

<div id="a11y-widget-container">
    
    <button id="a11y-trigger" aria-label="Abrir menú de accesibilidad">
        <img src="Cliente/accesibilidad/access.svg" alt="Icono de Accesibilidad">
    </button>
    
    <div id="a11y-menu-options" aria-hidden="true">
        <div class="a11y-group d-flex flex-column"> 
            <div class="a11y-header d-flex align-items-center justify-content-between mb-2"> 
                <h3 class="flex-fill m-0">Accesibilidad</h3>
                <button id="btn-reset-all" title="Restablecer todas las configuraciones" class="a11y-control-btn me-2">R</button>
                <button id="btn-cerrar" title="Cerrar Menú" class="a11y-control-btn">X</button>
            </div>
            <div class="d-flex flex-row mb-2">
                <button id="btn-increase-text" title="Aumentar Tamaño del Texto" class="flex-fill">A</button> 
                <button id="btn-decrease-text" title="Disminuir Tamaño del Texto" class="flex-fill mx-2">A</button> 
                <button id="btn-text-spacing" title="Aumentar Espaciado de Línea" class="flex-fill">Espaciado</button>
            </div>
                <button id="btn-high-contrast" title="Alternar Alto Contraste">Alto Contraste</button>
            <div class="d-flex flex-row mb-2">
                <button id="btn-color-blindness"  title="" class="flex-fill">deutera</button> 
                <button id="btn-color-blindness2" title="" class="flex-fill mx-2">prota</button> 
                <button id="btn-color-blindness3" title="" class="flex-fill">trita</button>
            </div>
            
            <button id="btn-seizure-safe" title="Desactivar Animaciones">Seguro Convulsión</button>
            <button id="btn-dyslexia" title="Alternar Fuente Amigable con Dislexia">Fuente Dislexia</button>
            <button id="btn-concentracion" title="Sector de concentracion">Concentracion</button>
        </div>
    </div>

</div>

<script src="Cliente/accesibilidad/accesibilidad.js"></script>
<script>
document.addEventListener("DOMContentLoaded", () => {
    const widget = document.getElementById("a11y-widget-container");
    document.documentElement.appendChild(widget); // MOVER AL <html>
});
</script>