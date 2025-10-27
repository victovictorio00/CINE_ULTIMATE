<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.ReservaTemporal" %>
<%@ page import="modelo.Producto" %>
<%@ page import="modelo.ProductoDao" %>
<%@ page import="java.util.*" %>
<%
    ReservaTemporal rt = (ReservaTemporal) session.getAttribute("reservaTemporal");
    @SuppressWarnings("unchecked")
    List<String> butacas = (List<String>) session.getAttribute("butacasSeleccionadas");
    @SuppressWarnings("unchecked")
    Map<Integer, Integer> productos = (Map<Integer, Integer>) session.getAttribute("productosSeleccionados");
    @SuppressWarnings("unchecked")
    Map<String,String> cliente = (Map<String,String>) session.getAttribute("clienteDatos");
    String metodoGuardado = (String) session.getAttribute("metodoPago");
    String nombreVal = cliente != null && cliente.get("nombre") != null ? cliente.get("nombre") : "";
    String emailVal  = cliente != null && cliente.get("email")  != null ? cliente.get("email") : "";
    String metodoVal = metodoGuardado != null ? metodoGuardado : "";

    ProductoDao pDao = new ProductoDao();
    double totalProductos = 0.0;
    List<Map<String,Object>> listaProductos = new ArrayList<>();

    if (productos != null && !productos.isEmpty()) {
        for (Map.Entry<Integer,Integer> e : productos.entrySet()) {
            Integer pid = e.getKey();
            Integer qty = e.getValue();
            Producto prod = null;
            try { prod = pDao.leer(pid); } catch (Exception ex) { prod = null; }

            // --- LECTURA SEGURA DEL PRECIO (soporta double primitivo o Double Object)
            double precio = 0.0;
            if (prod != null) {
                try {
                    // asignamos a Object para permitir tanto double primitivo (autobox) como Double
                    Object tmp = prod.getPrecio(); // si devuelve double se autoboxea a Double
                    if (tmp instanceof Number) {
                        precio = ((Number) tmp).doubleValue();
                    } else {
                        // en caso raro que no sea Number, intentar parse
                        try {
                            precio = Double.parseDouble(String.valueOf(tmp));
                        } catch (Throwable ignore) { precio = 0.0; }
                    }
                } catch (Throwable t) {
                    // fallback seguro
                    precio = 0.0;
                }
            }

            Map<String,Object> m = new HashMap<>();
            m.put("id", pid);
            m.put("nombre", prod != null ? prod.getNombre() : ("Producto #" + pid));
            m.put("cantidad", qty);
            m.put("precio", precio);
            m.put("subtotal", precio * qty);
            totalProductos += precio * qty;
            listaProductos.add(m);
        }
    }

    double totalEntradas = 0.0;
    if (rt != null && butacas != null) {
        totalEntradas = rt.getPrecioEntrada() * butacas.size();
    } else if (session.getAttribute("totalEntradas") != null) {
        try { totalEntradas = Double.parseDouble(session.getAttribute("totalEntradas").toString()); } catch (Exception ex) {}
    }
    double totalGeneral = totalEntradas + totalProductos;
    java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es","PE"));
    String totalEntradasFmt = fmt.format(totalEntradas);
    String totalProductosFmt = fmt.format(totalProductos);
    String totalGeneralFmt = fmt.format(totalGeneral);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>Método de pago - CineOnline</title>

    <link href="<%= request.getContextPath()%>/Estilos/peliculaClienteStyle.css" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body { background: #f5f7fa; font-family: 'Poppins', sans-serif; }
        .container-main { max-width:1100px; margin: 36px auto; padding: 0 12px; }
        .grid { display:grid; grid-template-columns: 1fr 420px; gap: 22px; align-items:start; }
        .card { background:#fff; border-radius:12px; padding:18px; box-shadow:0 10px 30px rgba(0,0,0,0.06); }
        h2 { margin:0 0 12px 0; font-weight:700; color:#222; }
        .small-muted { color:#6b7280; font-size:0.95rem; }
        .summary-row { display:flex; justify-content:space-between; margin:8px 0; color:#374151; }
        .section-title { font-size:1rem; font-weight:700; color:#111827; margin-bottom:8px; }
        .btn-primary { background: linear-gradient(90deg,#FF6B3A,#FF8A61); border:none; border-radius:8px; padding:12px 18px; font-weight:700; }
        .price-box { background:linear-gradient(90deg,#FF6B3A,#FF8A61); color:#fff; padding:12px 16px; border-radius:10px; display:inline-block; font-weight:800; }
        label { font-weight:600; margin-top:10px; display:block; }
        input, select { width:100%; padding:8px 10px; border-radius:8px; border:1px solid #d1d5db; margin-top:6px; }
        .hidden { display:none; }
        .product-line { display:flex; justify-content:space-between; padding:6px 0; border-bottom:1px dashed #e5e7eb; }
        .muted { color:#6b7280; }
        .field-error { color:#b91c1c; font-size:0.85rem; margin-top:6px; display:none; }
        @media (max-width:980px) {
            .grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
<div class="container-main">
    <div class="grid">
        <div class="card">
            <h2>Resumen de compra</h2>
            <p class="small-muted">Revisa tu selección antes de continuar con el pago.</p>
            <div style="margin-top:16px;">
                <div class="section-title">Función</div>
                <div class="small-muted">
                    <p><strong>Película:</strong> <%= (rt!=null ? rt.getIdPelicula() : "N/D") %></p>
                    <p><strong>Fecha/Hora:</strong> <%= (rt!=null && rt.getFecha()!=null ? new java.text.SimpleDateFormat("dd 'de' MMMM 'de' yyyy - HH:mm", new java.util.Locale("es","ES")).format(rt.getFecha()) : "N/D") %></p>
                    <p><strong>Sala:</strong> <%= (rt!=null ? rt.getIdSala() : "N/D") %></p>
                </div>
            </div>

            <div style="margin-top:14px;">
                <div class="section-title">Butacas</div>
                <div class="small-muted">
                    <%
                        if (butacas == null || butacas.isEmpty()) {
                    %>
                        <p>Ninguna butaca seleccionada</p>
                    <%
                        } else {
                    %>
                        <p><strong>Cantidad:</strong> <%= butacas.size() %></p>
                        <p><strong>Butacas:</strong> <%= String.join(", ", butacas) %></p>
                    <%
                        }
                    %>
                </div>
            </div>

            <div style="margin-top:14px;">
                <div class="section-title">Dulcería</div>
                <div>
                    <%
                        if (listaProductos.isEmpty()) {
                    %>
                        <p class="muted">No hay productos seleccionados</p>
                    <%
                        } else {
                            for (Map<String,Object> mp : listaProductos) {
                    %>
                        <div class="product-line">
                            <div>
                                <div style="font-weight:600;"><%= mp.get("nombre") %></div>
                                <div class="muted" style="font-size:0.9rem;">x <%= mp.get("cantidad") %></div>
                            </div>
                            <div style="text-align:right;">
                                <div style="font-weight:700;"><%= fmt.format((Double)mp.get("subtotal")) %></div>
                            </div>
                        </div>
                    <%
                            }
                        }
                    %>
                </div>
            </div>

            <div style="margin-top:18px;">
                <div class="section-title">Totales</div>
                <div class="summary-row">
                    <div class="muted">Entradas</div>
                    <div><strong><%= totalEntradasFmt %></strong></div>
                </div>
                <div class="summary-row">
                    <div class="muted">Productos</div>
                    <div><strong><%= totalProductosFmt %></strong></div>
                </div>
                <div style="border-top:1px solid #e6e9ee; margin-top:10px; padding-top:10px;">
                    <div class="summary-row">
                        <div class="section-title">Total</div>
                        <div class="price-box"><%= totalGeneralFmt %></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Método de pago</h2>
            <p class="small-muted">Selecciona cómo deseas pagar y completa tus datos.</p>

            <form id="metodoForm" method="post" action="<%= request.getContextPath() %>/ClienteServlet?action=metodoPago" novalidate>
                <label for="metodo">Método de pago</label>
                <select id="metodo" name="metodo" required>
                    <option value="" <%= metodoVal.isEmpty() ? "selected" : "" %>>-- Seleccionar --</option>
                    <option value="TARJETA" <%= "TARJETA".equals(metodoVal) ? "selected" : "" %>>Tarjeta (VISA/MC)</option>
                    <option value="EFECTIVO" <%= "EFECTIVO".equals(metodoVal) ? "selected" : "" %>>Efectivo</option>
                    <option value="YAPE" <%= "YAPE".equals(metodoVal) ? "selected" : "" %>>Yape / Transferencia</option>
                </select>

                <label for="nombre">Nombre completo</label>
                <input id="nombre" name="nombre" type="text" required placeholder="Ej. Juan Pérez" value="<%= nombreVal %>" />
                <div id="errNombre" class="field-error">Ingrese su nombre</div>

                <label for="email">Correo electrónico</label>
                <input id="email" name="email" type="email" required placeholder="ejemplo@correo.com" value="<%= emailVal %>" />
                <div id="errEmail" class="field-error">Ingrese un correo válido</div>

                <div id="tarjetaFields" class="<%= "TARJETA".equals(metodoVal) ? "" : "hidden" %>">
                    <label for="cardNumber">Número de tarjeta</label>
                    <input id="cardNumber" name="cardNumber" type="text" inputmode="numeric" placeholder="XXXX XXXX XXXX XXXX" maxlength="23" />
                    <div id="errCard" class="field-error">Número de tarjeta inválido</div>

                    <div class="row" style="margin-top:8px;">
                        <div class="col-7">
                            <label for="expiry">Vencimiento (MM/AA)</label>
                            <input id="expiry" name="expiry" type="text" placeholder="MM/AA" maxlength="5" />
                            <div id="errExpiry" class="field-error">Formato inválido (MM/AA)</div>
                        </div>
                        <div class="col-5">
                            <label for="cvv">CVV</label>
                            <input id="cvv" name="cvv" type="text" inputmode="numeric" maxlength="4" placeholder="123" />
                            <div id="errCvv" class="field-error">CVV inválido</div>
                        </div>
                    </div>
                    <p class="small-muted" style="margin-top:10px;">Los datos de tarjeta en esta demo no son procesados realmente.</p>
                </div>

                <div style="margin-top:14px;">
                    <button id="btnContinue" type="submit" class="btn-primary" style="width:100%;" disabled aria-disabled="true">Continuar</button>
                </div>
            </form>

            <div style="margin-top:12px; font-size:0.9rem; color:#6b7280;">
                Al continuar, revisarás la confirmación final antes de que la compra sea procesada.
            </div>
        </div>
    </div>
</div>

<script>
    (function(){
        const metodoSel = document.getElementById('metodo');
        const tarjetaFields = document.getElementById('tarjetaFields');
        const btnContinue = document.getElementById('btnContinue');
        const nombreIn = document.getElementById('nombre');
        const emailIn = document.getElementById('email');
        const cardInput = document.getElementById('cardNumber');
        const expiryInput = document.getElementById('expiry');
        const cvvInput = document.getElementById('cvv');

        // errores
        const errNombre = document.getElementById('errNombre');
        const errEmail = document.getElementById('errEmail');
        const errCard = document.getElementById('errCard');
        const errExpiry = document.getElementById('errExpiry');
        const errCvv = document.getElementById('errCvv');

        function showErr(el, show) {
            if (!el) return;
            el.style.display = show ? 'block' : 'none';
        }

        function onlyDigits(str) {
            return str.replace(/\D/g,'');
        }

        function isEmailValid(v) {
            return /\S+@\S+\.\S+/.test(v);
        }

        function validateCardFields() {
            let ok = true;

            const cardValRaw = cardInput ? cardInput.value.trim() : '';
            const cardDigits = onlyDigits(cardValRaw);
            if (!cardInput) ok = false;
            else {
                const len = cardDigits.length;
                if (!(len >= 13 && len <= 19)) { ok = false; showErr(errCard, true); }
                else showErr(errCard, false);
            }

            const exp = expiryInput ? expiryInput.value.trim() : '';
            if (!expiryInput) ok = false;
            else {
                const re = /^(0[1-9]|1[0-2])\/\d{2}$/;
                if (!re.test(exp)) { ok = false; showErr(errExpiry, true); }
                else showErr(errExpiry, false);
            }

            const cvv = cvvInput ? cvvInput.value.trim() : '';
            if (!cvvInput) ok = false;
            else {
                if (!/^\d{3,4}$/.test(cvv)) { ok = false; showErr(errCvv, true); }
                else showErr(errCvv, false);
            }

            return ok;
        }

        function toggleTarjeta() {
            const v = metodoSel.value;
            if (v === 'TARJETA') {
                tarjetaFields.classList.remove('hidden');
                if (cardInput) cardInput.setAttribute('data-required','1');
                if (expiryInput) expiryInput.setAttribute('data-required','1');
                if (cvvInput) cvvInput.setAttribute('data-required','1');
            } else {
                tarjetaFields.classList.add('hidden');
                if (cardInput) { cardInput.removeAttribute('data-required'); showErr(errCard,false); }
                if (expiryInput) { expiryInput.removeAttribute('data-required'); showErr(errExpiry,false); }
                if (cvvInput) { cvvInput.removeAttribute('data-required'); showErr(errCvv,false); }
            }
            validateForm();
        }

        function validateForm(){
            const metodo = metodoSel.value;
            const nombre = nombreIn.value.trim();
            const email = emailIn.value.trim();

            let ok = true;

            if (!nombre) { ok = false; showErr(errNombre, true); } else { showErr(errNombre, false); }
            if (!isEmailValid(email)) { ok = false; showErr(errEmail, true); } else { showErr(errEmail, false); }

            if (metodo === 'TARJETA') {
                if (!validateCardFields()) ok = false;
            }

            btnContinue.disabled = !ok;
            btnContinue.setAttribute('aria-disabled', String(!ok));
        }

        metodoSel.addEventListener('change', function(){
            toggleTarjeta();
        });
        nombreIn.addEventListener('input', validateForm);
        emailIn.addEventListener('input', validateForm);
        if (cardInput) cardInput.addEventListener('input', function(e){
            let v = cardInput.value;
            v = v.replace(/[^\d\s]/g,'');
            cardInput.value = v;
            validateForm();
        });
        if (expiryInput) expiryInput.addEventListener('input', function(e){
            let v = expiryInput.value.replace(/[^\d]/g,'');
            if (v.length >= 3) v = v.substring(0,2) + '/' + v.substring(2,4);
            expiryInput.value = v;
            validateForm();
        });
        if (cvvInput) cvvInput.addEventListener('input', function(){ validateForm(); });

        const form = document.getElementById('metodoForm');
        if (form) {
            form.addEventListener('submit', function(e){
                if (btnContinue.disabled) {
                    e.preventDefault();
                    return;
                }
                btnContinue.disabled = true;
                btnContinue.textContent = 'Procesando...';
            });
        }

        // Inicial
        toggleTarjeta();
        validateForm();
    })();
</script>

</body>
</html>
