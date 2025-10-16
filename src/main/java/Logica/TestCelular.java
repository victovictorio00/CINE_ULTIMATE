package Logica;
import java.util.regex.*;
        
public class TestCelular {
    
    private static final String TLF_REGEX = "^9[0-9]{8}";
    private static final Pattern NAME_PATTERN = Pattern.compile(TLF_REGEX);
    
    public static boolean numeroValido(String numero){
        if(numero == null) return false;
        Matcher matcher = NAME_PATTERN.matcher(numero);
        return matcher.matches();
    }
}
