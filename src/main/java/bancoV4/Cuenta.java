/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/

package bancoV4;

import java.io.Serializable;
//import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * @author Fer
*/

public class Cuenta implements Serializable {
    private String iban;
    private TipoCuenta tipoCuenta;
    private String dni;
    private String titular;
    private String correo;
    private double saldo;
//    private LocalDate fechaLiquidacion;
//    private double limiteCredito;
    
    // Inicio Constructor
    
    public Cuenta(String iban, TipoCuenta tipoCuenta, String dni, String titular, String correo) {
        this.iban = iban;
        this.tipoCuenta = tipoCuenta;
        this.dni = dni;
        this.titular = titular;
        this.correo = correo;
        this.saldo = 0;
//        this.fechaLiquidacion = LocalDate.now();
        // this.limiteCredito = this.tipoCuenta.;
    }
    
    public Cuenta(String iban, String titular, String dni, double saldo) {
        this.iban = iban;
        this.titular = titular;
        this.dni = dni;
        this.saldo = saldo;
//        this.fechaLiquidacion = LocalDate.now();
    }
    
    public Cuenta() {
        this.iban = "";
        this.titular = "";
        this.dni = "";
        this.correo = "";
        this.saldo = 0;
    }
    
    public Cuenta(String dni) {
        this.dni = "";
    }
    
    // Fin Constructor
    
    // Inicio Getters y Setters

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
//    public LocalDate getFechaLiquidacion() {
//        return fechaLiquidacion;
//    }
//
//    public void setFechaLiquidacion(LocalDate fechaLiquidacion) {
//        this.fechaLiquidacion = fechaLiquidacion;
//    }
//
//    public double getLimiteCredito() {
//        return limiteCredito;
//    }
//
//    public void setLimiteCredito(double limiteCredito) {
//        this.limiteCredito = limiteCredito;
//    }
    
    // Fin Getters y Setters
    
    public static boolean validarCorreo(String correo) throws Exception {
        String expRegularCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // expresion de prueba, hay que mejorar
        Pattern patronCorreo = Pattern.compile(expRegularCorreo);
        
        if (patronCorreo.matcher(correo).matches()) {
            return true;
        } else {
            throw new Exception("Error: formato de correo electronico invalido");
        }
    }
    
    public static boolean validarDNI(String dniNIE) throws Exception {
        if (dniNIE.length() != 9) {
            throw new Exception("El DNI debe de tener 9 caracteres");
        } else {
            String expNIF = "\\d{8}[A-Z]"; // Debe tener 8 digitos y despues una letra de la A a la Z
            Pattern patronNIF = Pattern.compile(expNIF);
            String expNIE = "[XYZ0-9]\\d{7}[A-Z]"; // Debe tener una letra (X, Y o Z) o un numero del 0 al 9, despues 7 numeros y una letra de la A a la Z
            Pattern patronNIE = Pattern.compile(expNIE);

            if (patronNIF.matcher(dniNIE).matches()) {
                char letra = calcularLetra(Integer.parseInt(dniNIE.substring(0, 8))); // Coge los 8 numeros, los transforma a entero y de ahi calcula la letra con el metodo
                
                if (letra == dniNIE.charAt(8)) { // Si se corresponde la letra que se calcula con el caracter 9 (el 8 empezando desde el 0) es verdadero
                    return true;
                } else { // Si no es falso y lanza excepcion
                    throw new Exception("La letra del NIF no es válida.");
                }
            } else if (patronNIE.matcher(dniNIE).matches()) {
                // Transformamos el primer caracter con el metodo convertir, despues lo juntamos con los demas digitos, lo convertimos a entero y calculamos la letra con el metodo, abulta
                char letra = calcularLetra(Integer.parseInt(convertirLetraInicial(dniNIE.charAt(0)) + dniNIE.substring(1, 8)));
                
                if (letra == dniNIE.charAt(8)) { // Si se corresponde la letra que se calcula con el caracter 9 (el 8 empezando desde el 0) es verdadero
                    return true;
                } else { // Si no es falso y lanzamos excepcion
                    throw new Exception("Error: La letra del NIE no es válida.");
                }
            } else { // Directamente si no se ajusta el parametro con las 2 expresiones regulares lanza excepcion de formato
                throw new Exception("Error: formato de documento inválido.");
            }
        }
    }

    private static char calcularLetra(int dniNIE) {
        final String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        
        return Character.toUpperCase(letras.charAt(dniNIE % 23));
    }

    private static String convertirLetraInicial(char letraInicial) throws Exception {
        switch (Character.toUpperCase(letraInicial)) {
            case 'X':
                return "0";
            case 'Y':
                return "1";
            case 'Z':
                return "2";
            default:
                throw new Exception("No se ha podido convertir la letra inicial.");
        }
    }
    
    public void ingresarDinero(double cantidad) {
        if (cantidad > 0) {
            this.saldo += cantidad;
        }
    }
    
    public void retirarDinero(double cantidad) {
        this.saldo -= cantidad;
    }
    
//    @Override
//    public String toString() {
//        return String.format("%s, %-30s, %.2f", this.iban, this.titular, this.saldo);
//    }
}
