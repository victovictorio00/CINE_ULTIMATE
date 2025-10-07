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
public class TestUsername {
    private static final String USERNAME_REGEX = "^[A-Za-z][A-Za-z0-9._]{3,19}$";
    private static final Pattern PATTERN = Pattern.compile(USERNAME_REGEX);

    public static boolean esValida(String Username) {
        if (Username == null) 
            return false;
        Matcher matcher = PATTERN.matcher(Username);
        return matcher.matches();
    }
}
