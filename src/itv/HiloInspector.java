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
    private static final String MENSAJE_DESPEDIDA = "FIN";
    private static final String MENSAJE_CIERRE = "Cerrado";

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

        for (String frase : frases) {
            if (frase.equalsIgnoreCase(elemento)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {

        String linea = null;
        String nombreVehiculo;
        String respuesta;
        String numeroPruebas;
        long tiempoPrueba;
        int probabilidad = 60;
        String resultadoPrueba;
        String[] resultados = new String[pruebas.length];
        Socket socket;

        // Se implementa un bucle do-while para que se ejecute al menos una vez y siempre y cuando no se reciba el mensaje de cierre por parte del hilo jefe.
        do {
            try {
                // El hilo inspector solicita la entrada de un vehículo en la línea de inspección
                socket=rcITV.solicitarVehiculo();

                // Se generan los flujos de entrada y salida, comunicación servidor-cliente (hilo inspector - hilo coche)
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

                // Se implementea un bucle while para establecer la comunicación siempre y cuando el hilo coche no devuelva null o el mensaje de despedida
                while ((linea = br.readLine()) != null && !linea.equalsIgnoreCase(MENSAJE_DESPEDIDA)) {
                    // El inspector recibe el nombre del vehículo
                    nombreVehiculo = linea;
                    // El inspector recepciona el vehículo
                    System.out.println(String.format("%s entra en la ITV.",nombreVehiculo));
                    // El inspector responde con un mensaje de bienvenida
                    respuesta = "Buenas tardes, le dejo aquí el walkie‑talkie para darle órdenes";
                    pw.println(respuesta);
                    numeroPruebas = String.valueOf(pruebas.length);
                    pw.println(numeroPruebas);

                    // Pruebas
                    for (int i = 0; i < pruebas.length; i++) {
                        tiempoPrueba = (long) ((Math.random() * 5 + 1) * 1000);
                        Thread.sleep(tiempoPrueba);
                        respuesta = String.format("Realice la prueba: %s", pruebas[i]);
                        pw.println(respuesta);
                        // El inspector recibe la respuesta del cliente y comprueba si se trata de una frase inadecuada
                        System.out.println(linea);
                        if (comprobarElementoLista(linea, frasesInadecuadas)) {
                            probabilidad -= 10;
                        }
                        if (Math.random() * 100 < probabilidad) {
                            resultadoPrueba = "Si";
                            resultados[i] = resultadoPrueba;
                        } else {
                            resultadoPrueba = "No";
                            resultados[i] = resultadoPrueba;
                        }
                    }

                    // Mensaje de despedida
                    respuesta = "Le retiro el walkie, esperé en la puerta. Gracias.";
                    pw.println(respuesta);

                    // Mensaje de presentación de resultados de la prueba
                    if (!comprobarElementoLista("No", resultados)) {
                        pw.println("Prueba superada" + nombreVehiculo);
                    } else {
                        pw.println("Prueba no superada" + nombreVehiculo);
                    }   

                    // Cierre de recursos
                    s.close();
                    br.close();
                    pw.close();
                }

            } catch (InterruptedException e) {
                System.err.println("Hilo interrumpido: " + e.getMessage());
                Thread.currentThread().interrupt();
            } catch (SocketException e) {
                System.err.println("Error de conexión: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error en el hilo servidor E/S: " + e.getMessage());
            }
        } while (!linea.equalsIgnoreCase(MENSAJE_CIERRE));
    }
}
