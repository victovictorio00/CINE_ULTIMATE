/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Desktop
 */
public class TestPassword {
    private static final String PASSWORD_REGEX = "^[A-Z][A-Za-z0-9-_.]{5,29}$";//validamos de q inicie en mayuscula
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static boolean esValida(String Password) {
        if (Password == null){
            return false;
        }
        Matcher matcher = PATTERN.matcher(Password);
        return matcher.matches();
    }
}
