package br.upe.aula.controlepeso;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class teste {

    public static void main(String[] args) {
        System.out.println(isValidEmailAddress("garbosoftware@gmail.com"));
        System.out.println(isValidEmailAddress("aaabbb@gmail.com"));
        System.out.println(isValidEmailAddress("invalido@com.br"));
        System.out.println(isValidEmailAddress("inv@lido@com.br"));
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
