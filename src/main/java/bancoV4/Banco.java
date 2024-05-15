package bancoV4;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

@XmlRootElement
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private LinkedHashSet<Cuenta> cuentas;
    private int numeroCuentas;
    private static final int MAX_CUENTAS = 100;
    
    public Banco() {
        this.cuentas = new LinkedHashSet<>();
    }

    public Banco(String nombre) {
        this.nombre = nombre;
        this.cuentas = new LinkedHashSet<>();
        this.numeroCuentas = cuentas.size();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LinkedHashSet<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(LinkedHashSet<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public int getNumeroCuentas() {
        return numeroCuentas;
    }

    public void setNumeroCuentas(int numeroCuentas) {
        this.numeroCuentas = numeroCuentas;
    }

    public boolean agregarCuenta(String codigo, TipoCuenta tipoCuenta, String dni, String nombreTitular, String correo) {
        if (this.numeroCuentas >= MAX_CUENTAS) {
            return false;
        } else {
            this.cuentas.add(new Cuenta(codigo, tipoCuenta, dni, nombreTitular, correo));
            this.numeroCuentas++;
            return true;
        }
    }
    
    public void guardarAXML(String nombreArchivo) {
        try {
            JAXBContext context = JAXBContext.newInstance(Banco.class);
            Marshaller marshaller = context.createMarshaller();
            
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, new File(nombreArchivo));
            
            System.out.println("Ya se ha exportado todo a XML");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    public static Banco recuperarXML(String nombreArchivo) {
        try {
            JAXBContext context = JAXBContext.newInstance(Banco.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            Banco banco = (Banco) unmarshaller.unmarshal(new File(nombreArchivo));
            
            System.out.println("Se han importado los datos del XML");
            
            return banco;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int importarCuentasCSV(String archivo) throws FileNotFoundException, IOException {
        int cuentasImportadas = 0;

        BufferedReader csvCuentas = new BufferedReader(new FileReader(archivo));
        String linea;

        while ((linea = csvCuentas.readLine()) != null) {
            String[] delimitador = linea.split(";");

            if (delimitador.length == 4) {
                String numeroCuenta = delimitador[0];
                String titular = delimitador[1];
                String dni = delimitador[2];
                double saldo = Double.parseDouble(delimitador[3]);

                cuentas.add(new Cuenta(numeroCuenta, titular, dni, saldo));
                cuentasImportadas++;
            }
        }
        
        System.out.println("Se han importado " + cuentasImportadas + " cuentas");
        
        return cuentasImportadas;
    }
    
    public int importarMovimientosCSV(String archivo) throws FileNotFoundException, IOException {
        int movimientosHechos = 0;
        
        BufferedReader csvMovimientos = new BufferedReader(new FileReader(archivo));
        String linea;
        
        while ((linea = csvMovimientos.readLine()) != null) {            
            String[] bloques = linea.split(";");
            
            if (bloques.length == 3) {
                String numeroCuenta = bloques[0];
                String tipoOperacion = bloques[1];
                double cantidad = Double.parseDouble(bloques[2]);
                
                if (existeCuenta(numeroCuenta)) {
                    if ("ingreso".equals(tipoOperacion)) {
                        ingresar(numeroCuenta, cantidad);
                    } else if ("reintegro".equals(tipoOperacion)) {
                        retirar(numeroCuenta, cantidad);
                    }
                }
                
                movimientosHechos++;
            }
        }
        
        System.out.println("Se han hecho " + movimientosHechos + " movimientos");
        
        return movimientosHechos;
    }

    public void cargarDatos() {
        long inicio = System.currentTimeMillis();
        String iban, dni, titular, correo;
        TipoCuenta[] tiposCuenta = TipoCuenta.values();
        Random tipoAleatorio = new Random();

        for (int i = 0; i < MAX_CUENTAS; i++) {
            iban = String.format("IBAN%03d", (i + 1));
            dni = String.format("%08dA", (i + 1));
            titular = String.format("Titular%03d", (i + 1));
            correo = String.format("ejemplo%03d@gmail.com", (i + 1));
            TipoCuenta tipoCuenta = tiposCuenta[tipoAleatorio.nextInt(tiposCuenta.length)]; // coge un numero entre el 0 y la longitud del array

            agregarCuenta(iban, tipoCuenta, dni, titular, correo);
        }

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo transcurrido en milisegundos: " + (fin - inicio));
    }

    public String consultarCuenta(String codigo) {
        StringBuilder consulta = new StringBuilder();

        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIban().equals(codigo)) {
                consulta.append("IBAN: ").append(codigo).append("\t\t")
                        .append("Tipo de cuenta: ").append(cuenta.getTipoCuenta()).append("\t\t")
                        .append("Titular: ").append(cuenta.getTitular()).append("\t\t")
                        .append(cuenta.getDni()).append("\t\t")
                        .append(cuenta.getCorreo()).append("\t\t")
                        .append("Saldo: ").append(cuenta.getSaldo());

                return consulta.toString();
            }
        }

        consulta.append("La cuenta indicada no se ha encontrado");
        return consulta.toString();
    }

    public void borrarCuenta(String codigo) throws Exception {
        boolean borrada = false;

        Iterator<Cuenta> iterador = cuentas.iterator();
        while (iterador.hasNext()) {
            Cuenta cuenta = iterador.next();
            if (cuenta.getIban().equals(codigo)) {
                iterador.remove();
                borrada = true;
                numeroCuentas--;
            }
        }

        if (!borrada) {
            throw new Exception("Error: No se ha encontrado la cuenta");
        }
    }

    public boolean existeCuenta(String codigo) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIban().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    public String listadoCuentas() {
        if (cuentas.isEmpty()) {
            return "No hay cuentas en el banco";
        }

        StringBuilder listado = new StringBuilder();
        listado.append("Total de cuentas: ").append(numeroCuentas).append("\n");

        for (Cuenta cuenta : cuentas) {
            listado.append("IBAN: ").append(cuenta.getIban()).append("\t\t")
                    .append("Tipo de cuenta: ").append(cuenta.getTipoCuenta()).append("\t\t")
                    .append("Titular: ").append(cuenta.getTitular()).append("\t\t")
                    .append(cuenta.getDni()).append("\t\t")
                    .append(cuenta.getCorreo()).append("\t\t")
                    .append("Saldo: ").append(cuenta.getSaldo())
                    .append("\n");
        }

        return listado.toString();
    }

    public double informaDeSaldo(String iban) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIban().equals(iban)) {
                return cuenta.getSaldo();
            }
        }
        return -100000000;
    }

    public boolean ingresar(String codigo, double importe) {
        Cuenta cuenta = localizarCuenta(codigo);
        if (cuenta == null) {
            return false;
        } else {
            cuenta.ingresarDinero(importe);
            return true;
        }
    }

    public boolean retirar(String codigo, double importe) {
        Cuenta cuenta = localizarCuenta(codigo);
        if (cuenta == null) {
            return false;
        } else {
            cuenta.retirarDinero(importe);
            return true;
        }
    }

    public void guardarBanco(String nombreArchivo) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            throw new IOException("Error: No se ha podido guardar el archivo: " + nombreArchivo + "\n" + e);
        }
    }

    public static Banco cargarEstado(String nombreArchivo) throws ClassNotFoundException, IOException {
        Banco miBanco;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            miBanco = (Banco) inputStream.readObject();
        } catch (IOException e) {
            throw new IOException("Error: No se ha podido leer el archivo: " + nombreArchivo + "\n" + e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Error: No se ha podido abrir el archivo: " + nombreArchivo + "\n" + e);
        }
        return miBanco;
    }

    private Cuenta localizarCuenta(String codigo) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIban().equals(codigo)) {
                return cuenta;
            }
        }
        return null;
    }
}
