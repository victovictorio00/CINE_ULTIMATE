package Logica;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validación de precio para Producto.
 * Reglas:
 * - Número positivo o cero
 * - Hasta 6 enteros + 2 decimales (ej: 999999.99)
 * - Opcionalmente con 1 o 2 decimales
 */
public class TestPrecio {
    private static final String PRECIO_REGEX = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,2})?$";
    private static final Pattern PATTERN = Pattern.compile(PRECIO_REGEX);

    public static boolean esValido(String precio) {
        if (precio == null) 
            return false;
        Matcher matcher = PATTERN.matcher(precio);
        return matcher.matches();
    }
}
