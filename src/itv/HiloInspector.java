/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author anaranjo
 */
public class HiloInspector implements Runnable {

    // Declaración de variables
    private Socket s;
    private final RecursoCompartidoITV rcITV;
    private static final String[] frasesInadecuadas = {
        "ok jefe",
        "lo que tú digas",
        "a mandar",
        "como usted mande",
        "vamos al lío",
        "marchando",
        "manda usted",
        "perfecto máquina",
        "de lujo"
    };
    private static final String[] pruebas = {
        "Luces",
        "Frenos",
        "Emisiones",
        "Dirección",
        "Suspensión"
    };
    private static final String[] mensajeFinal = {
        "Tome su pegatina",
        "Debe volver de nuevo"
    };

    // Método constructor del HiloInspector
    public HiloInspector(RecursoCompartidoITV rcITVinfierno) {
        this.rcITV = rcITVinfierno;
    }

    // Método para acceder al objeto Socket con el cual se establecerá la conexión con el hilo cliente
    public Socket getSocket() {
        return s;
    }

    // Método para establecer el objeto Socket que el hilo servidor debe recibir para establecer la conexión con el cliente
    public void setSocket(Socket s) {
        this.s = s;
    }

    // Método para comprobar si un elemento se encuentra almacenado en una lista
    private boolean comprobarElementoLista(String elemento, String[] frases) {

        // Se usa el método equalsIgnoreCase() dentro de un bucle for para no distinguir entre mayúsculas y minúsculas
        for (String frase : frases) {
            if (frase.equalsIgnoreCase(elemento)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {

        String nombreVehiculo;
        String codigoTurno;
        String mensajeOut;
        String mensajeIn;
        String numeroPruebas;
        long tiempoPrueba;
        int probabilidad;
        String resultadoPrueba;
        String[] resultados;
        TicketInspeccion ti;

        // Se implementa un bucle infinito while para que los hilos inspectores no mueran nunca.
        while (true) {
            // Se inician/instancian las variables
            resultados = new String[pruebas.length];
            probabilidad = 60;
            try {
                // El hilo inspector solicita la entrada de un vehículo en la línea de inspección
                ti = rcITV.solicitarVehiculo();

                // Se generan los flujos de entrada y salida, comunicación servidor-cliente (hilo inspector - hilo coche)
                BufferedReader br = new BufferedReader(new InputStreamReader(ti.getSocket().getInputStream()));
                PrintWriter pw = new PrintWriter(ti.getSocket().getOutputStream(), true);

                // El inspector envía el nombre del vehículo para que éste pueda renombrarse
                nombreVehiculo = ti.getNombreVehiculo();
                codigoTurno=ti.getCodigoTurno();
//ENVIADO 1
                pw.println(nombreVehiculo+" - "+codigoTurno);

                // El inspector recepciona el vehículo
                System.out.println(String.format("%s entra en la ITV.", nombreVehiculo));
                // El inspector responde con un mensaje de bienvenida
                mensajeOut = "Buenas tardes, le dejo aquí el walkie‑talkie para darle órdenes";
//ENVIADO 2
                pw.println(mensajeOut);
                numeroPruebas = String.valueOf(pruebas.length);
//ENVIADO 3
                pw.println(numeroPruebas);

                // Pruebas
                for (int i = 0; i < pruebas.length; i++) {
                    tiempoPrueba = (long) ((Math.random() * 5 + 1) * 1000);
                    Thread.sleep(tiempoPrueba);
                    mensajeOut = String.format("%s Realice la prueba: %s", nombreVehiculo, pruebas[i]);
                    // ENVIADO 4
                    pw.println(mensajeOut);
                    // El inspector recibe la mensajeOut del cliente y comprueba si se trata de una frase inadecuada
//RECIBIDO 1
                    mensajeIn = br.readLine();
                    System.out.println(String.format("Frase del %s: %s",nombreVehiculo, mensajeIn));
                    if (comprobarElementoLista(mensajeIn, frasesInadecuadas)) {
                        probabilidad -= 10;
                    }
                    if ((int) (Math.random() * 100) < probabilidad) {
                        resultadoPrueba = "Si";
                        resultados[i] = resultadoPrueba;
                    } else {
                        resultadoPrueba = "No";
                        resultados[i] = resultadoPrueba;
                    }
                }

                // Mensaje de despedida
                mensajeOut = "Le retiro el walkie, esperé en la puerta. Gracias.";
//ENVIADO 5
                pw.println(mensajeOut);
//ENVIADO 6
                // Mensaje de presentación de resultados de la prueba
                if (!comprobarElementoLista("No", resultados)) {
                    pw.println("Prueba superada" + nombreVehiculo);
                    pw.println(mensajeFinal[0]);
                } else {
                    pw.println("Prueba no superada" + nombreVehiculo);
                    pw.println(nombreVehiculo+" "+mensajeFinal[1]);
                }

                // Se libera una línea de inspección
                rcITV.salidaVehiculo();
                // Cierre de recursos
                ti.getSocket().close();
                br.close();
                pw.close();

            } catch (InterruptedException e) {
                System.err.println("Hilo interrumpido: " + e.getMessage());
                Thread.currentThread().interrupt();
            } catch (SocketException e) {
                System.err.println("Error de conexión: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error en el hilo servidor E/S: " + e.getMessage());
            }

        }
    }
}
