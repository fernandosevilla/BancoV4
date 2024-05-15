/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancoV4;

import static bancoV4.Banco.recuperarXML;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Fer
 */
public class Principal {

    public static void main(String[] args) throws Exception {
        try (Scanner teclado = new Scanner(System.in)) {
            Banco miBanco = null;
            int opcion;
            
            do {
                System.out.println("1. Crear Banco");
                System.out.println("2. Agregar Cuenta");
                System.out.println("3. Ingresar en Cuenta");
                System.out.println("4. Retirar de Cuenta");
                System.out.println("5. Ver Datos de Cuenta");
                System.out.println("6. Ver Informe Total del Banco");
                System.out.println("7. Borrar Cuenta");
                System.out.println("8. Guardar Datos");
                System.out.println("9. Cargar Datos");
                System.out.println("10. Importar cuentas CSV");
                System.out.println("11. Importar movimientos CSV");
                System.out.println("12. Exportar datos a XML");
                System.out.println("13. Importar datos de un XML");
                System.out.println("14. Salir");
                System.out.print("Selecciona una opción: ");
                
                opcion = teclado.nextInt();
                teclado.nextLine();
                
                switch (opcion) {
                    case 1:
                        if (miBanco != null) {
                            System.out.println("Ya existe un banco, escribe (si) si quieres agregar uno nuevo, escribe cualquier otra cosa para cancelar");
                            String crearBanco = teclado.nextLine().toLowerCase();
                            if (crearBanco.equals("si")) {
                                System.out.print("Ingresa el nombre del banco: ");
                                String nombreBanco = teclado.nextLine();
                                if (nombreBanco.equals(miBanco.getNombre())) {
                                    System.out.println("El banco que estás creando es el mismo que ya existe");
                                } else {
                                    if (nombreBanco.length() <= 0) {
                                        System.out.println("El nombre del banco esta vacio");
                                    } else {
                                        miBanco = new Banco(nombreBanco);
                                        System.out.println("El banco " + nombreBanco + " ha sido creado");
                                    }
                                }
                            } else {
                                break;
                            }
                        } else {
                            System.out.print("Ingresa el nombre del banco: ");
                            String nombreBanco = teclado.nextLine();
                            if (nombreBanco.length() <= 0) {
                                System.out.println("El nombre del banco está vacío");
                            } else {
                                miBanco = new Banco(nombreBanco);
                                System.out.println("El banco " + nombreBanco + " ha sido creado");
//                                miBanco.cargarDatos();
                            }
                        }
                        break;
                    case 2:
                        if (miBanco != null) {
                            if (miBanco.getNumeroCuentas() >= 100) {
                                System.out.println("No se pueden agregar mas de 100 cuentas");
                            } else {
                                System.out.print("Ingresa el IBAN de la cuenta: ");
                                String iban = teclado.nextLine();
                                
                                System.out.print("Ingresa el DNI del titular: ");
                                String dni = teclado.nextLine().toUpperCase();
                                
                                System.out.print("Ingresa el nombre del titular: ");
                                String titular = teclado.nextLine();
                                
                                System.out.print("Ingresa el correo electrónico del titular: ");
                                String correo = teclado.nextLine().toLowerCase();
                                
                                System.out.println("Elige un tipo de cuenta: ");
                                System.out.println("1. Ahorro");
                                System.out.println("2. Nomina");
                                System.out.println("3. Credito");
                                System.out.println("4. Valores");
                                
                                int tipo = teclado.nextInt();
                                teclado.nextLine();
                                TipoCuenta tipoCuenta = null;
                                
                                switch (tipo) {
                                    case 1:
                                        tipoCuenta = TipoCuenta.AHORRO;
                                        break;
                                    case 2:
                                        tipoCuenta = TipoCuenta.NOMINA;
                                        break;
                                    case 3:
                                        tipoCuenta = TipoCuenta.CREDITO;
                                        break;
                                    case 4:
                                        tipoCuenta = TipoCuenta.VALORES;
                                        break;
                                    default:
                                        throw new Exception("No has seleccionado correctamente el tipo de cuenta");
                                }
                                
                                try {
                                    if (Cuenta.validarDNI(dni) && Cuenta.validarCorreo(correo) && tipoCuenta != null) {
                                        miBanco.agregarCuenta(iban, tipoCuenta, dni, titular, correo); // Se crea la cuenta
                                        System.out.println("La cuenta " + iban + " del titular " + titular + " se ha agregado correctamente");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                            }
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        break;
                    case 3:
                        try {
                            if (miBanco != null) {
                                System.out.print("Ingresa IBAN de la cuenta: ");
                                String ibanIngreso = teclado.nextLine();
                                
                                System.out.print("Ingresa la cantidad a ingresar: ");
                                double cantidadIngreso = teclado.nextDouble();
                                teclado.nextLine();
                                
                                if (miBanco.ingresar(ibanIngreso, cantidadIngreso)) {
                                    System.out.println("Se han ingresado " + cantidadIngreso + "€. En total hay: " + miBanco.informaDeSaldo(ibanIngreso) + "€");
                                } else {
                                    System.out.println("No se ha hecho el ingreso. Quiza has escrito mal el IBAN");
                                }
                            } else {
                                System.out.println("No hay ningun banco creado");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("ERROR: Debes escribir un numero");
                            System.out.println(e);
                            
                            teclado.nextLine();
                            System.out.println();
                        } catch (IllegalArgumentException e) {
                            System.out.println(e);
                        }
                        break;
                    case 4:
                        try {
                            if (miBanco != null) {
                                System.out.print("Ingresa el IBAN de la cuenta: ");
                                String ibanRetiro = teclado.nextLine();
                                
                                System.out.print("Ingresa la cantidad a retirar: ");
                                double cantidadRetiro = teclado.nextDouble();
                                teclado.nextLine();
                                
                                if (miBanco.retirar(ibanRetiro, cantidadRetiro)) {
                                    System.out.println("Se han retirado " + cantidadRetiro + "€. Queda en total: " + miBanco.informaDeSaldo(ibanRetiro) + "€");
                                } else {
                                    System.out.println("No se ha retirado nada. Quiza has escrito mal el IBAN");
                                }
                            } else {
                                System.out.println("No hay ningun banco creado");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("ERROR: Debes escribir un numero");
                            System.out.println(e);
                            
                            teclado.nextLine();
                            System.out.println();
                        } catch (IllegalArgumentException e) {
                            System.out.println(e);
                        }
                        break;
                    case 5:
                        if (miBanco != null) {
                            System.out.print("Ingresa el IBAN de la cuenta: ");
                            String ibanConsulta = teclado.nextLine();
                            
                            System.out.println(miBanco.consultarCuenta(ibanConsulta));
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        
                        break;
                    case 6:
                        if (miBanco != null) {
                            System.out.println("Listado de cuentas:");
                            System.out.println(miBanco.listadoCuentas());
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        
                        break;
                    case 7:
                        if (miBanco != null) {
                            System.out.print("Ingresa el IBAN de la cuenta que quieres borrar: ");
                            String ibanBorrar = teclado.nextLine().toUpperCase();
                            
                            if (miBanco.existeCuenta(ibanBorrar)) {
                                miBanco.borrarCuenta(ibanBorrar);
                                System.out.println("La cuenta con IBAN " + ibanBorrar + " ha sido borrada del banco " + miBanco.getNombre());
                            } else {
                                System.out.println("La cuenta que has introducido (" + ibanBorrar + ") no existe");
                            }
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        
                        break;
                    case 8:
                        if (miBanco != null) {
                            System.out.print("Escribe el nombre del archivo donde se va a guardar: ");
                            String nombreArchivo = teclado.nextLine() + ".dat";
                            
                            miBanco.guardarBanco(nombreArchivo);
                            System.out.println("El banco se ha guardado bien en el archivo");
                        } else {
                            System.out.println("No puedes guardar un banco que no está creado");
                        }
                        
                        break;
                    case 9:
                        System.out.print("Escribe el nombre del archivo que corresponda al banco que quieres cargar: ");
                        String nombreArchivo = teclado.nextLine() + ".dat";
                        
                        miBanco = Banco.cargarEstado(nombreArchivo);
                        System.out.println("El archivo se ha cargado bien");
                        
                        break;
                    case 10:
                        if (miBanco != null) {
                            System.out.print("Escribe el nombre del CSV para importar cuentas: ");
                            String nombreCuentasCSV = teclado.nextLine() + ".csv";
                            
                            miBanco.importarCuentasCSV(nombreCuentasCSV);
                            
                            System.out.println("Se han importado correctamente las cuentas");
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        break;
                    case 11:
                        if (miBanco != null) {
                            System.out.print("Escribe el nombre del CSV para importar movimientos en las cuentas: ");
                            String nombreMovimientosCSV = teclado.nextLine() + ".csv";
                            
                            miBanco.importarMovimientosCSV(nombreMovimientosCSV);
                            
                            System.out.println("Se han importado correctamente los movimientos");
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        break;
                    case 12:
                        if (miBanco != null) {
                            System.out.print("Escribe el nombre del archivo XML donde quieras guardar este banco: ");
                            String nombreXML = teclado.nextLine() + ".xml";
                            
                            miBanco.guardarAXML(nombreXML);
                        } else {
                            System.out.println("No hay ningun banco creado");
                        }
                        break;
                    case 13:
                        if (miBanco != null) {
                            System.out.println("Ya existe un banco");
                        } else {
                            System.out.print("Escribe el nombre del archivo XML de donde recogerar los datos: ");
                            String nombreXML = teclado.nextLine() + ".xml";
                            miBanco = recuperarXML(nombreXML);
                        }
                        
                        break;
                    case 14:
                        break;
                }
            } while (opcion != 14);
        }
    }
}
