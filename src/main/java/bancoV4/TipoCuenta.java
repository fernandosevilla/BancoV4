/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package bancoV4;

/**
 * @author Fer
*/

public enum TipoCuenta {
    AHORRO(2.5, 0, 7.5, 0.05, 0),
    NOMINA(0.5, 0, 7.5, 0.1, 0),
    CREDITO(0.1, 4.5, 9.5, 0.5, 10000),
    VALORES(0, 6, 12, 0.65, 5000);
    
    private final double deudor;
    private final double acreedor;
    private final double descubierto;
    private final double gastoDia;
    private final int limiteCredito;

    private TipoCuenta(double deudor, double acreedor, double descubierto, double gastoDia, int limiteCredito) {
        this.deudor = deudor;
        this.acreedor = acreedor;
        this.descubierto = descubierto;
        this.gastoDia = gastoDia;
        this.limiteCredito = limiteCredito;
    }

    public double getDeudor() {
        return deudor;
    }

    public double getAcreedor() {
        return acreedor;
    }

    public double getDesc() {
        return descubierto;
    }
    
    public double getGastoDia() {
        return gastoDia;
    }

    public int getLimiteCredito() {
        return limiteCredito;
    }
}
