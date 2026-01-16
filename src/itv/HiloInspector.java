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
 * Clase Hilo Inspector
 * 
 * @author Antonio Naranjo Castillo
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

    // Método run del HiloInspector
    @Override
    public void run() {

        // Declaración de variables
        String nombreVehiculo;
        String codigoTurno;
        String mensajeOut;
        String mensajeIn;
        String numeroPruebas;
        long tiempoPrueba;
        int probabilidad;
        int resultado;
        String resultadoPrueba;
        String[] resultados;
        TicketInspeccion ti;

        // Se implementa un bucle infinito while para que los hilos inspectores no mueran nunca, estación ITV 24/7 operativa.
        while (true) {
            // Se inician/instancian las variables
            resultados = new String[pruebas.length];
            probabilidad = 60;
            
            try {
                // El hilo inspector solicita la entrada de un vehículo en la línea de inspección (solicita el ticket de inspección)
                ti = rcITV.solicitarVehiculo();

                // Se generan los flujos de entrada y salida del hilo inspector, comunicación servidor-cliente (hilo inspector - hilo coche)
                BufferedReader br = new BufferedReader(new InputStreamReader(ti.getSocket().getInputStream()));
                PrintWriter pw = new PrintWriter(ti.getSocket().getOutputStream(), true);

                // El inspector envía el nombre del vehículo para que éste pueda renombrarse
                nombreVehiculo = ti.getNombreVehiculo();
                // Se genera el código de turno para comprobar que el sistema operativo atiendo a cada hilo de manera aleatoria
                codigoTurno=String.valueOf(RecursoCompartidoITV.codigoTurno++);
//ENVIADO 1: Se envía mensaje al hilo coche
                pw.println(nombreVehiculo);
//ENVIADO 2: Se envía mensaje al hilo coche
                pw.println(codigoTurno);

                // El inspector recepciona el vehículo
                System.out.println(String.format("%s entra en la ITV.", nombreVehiculo));
                // El inspector responde con un mensaje de bienvenida
                mensajeOut = "Buenas tardes, le dejo aquí el walkie‑talkie para darle órdenes";
//ENVIADO 3: Se envía mensaje al hilo coche
                pw.println(mensajeOut);
                
                // El inspector envía al hilo coche el número de pruebas que tienen que realizar
                numeroPruebas = String.valueOf(pruebas.length);
//ENVIADO 4: Se envía mensaje al hilo coche
                pw.println(numeroPruebas);

                // Pruebas: Se implementa un bucle for para atender a cada una de las pruebas
                for (int i = 0; i < pruebas.length; i++) {
                    // Se asigna un tiempo aleatorio entre 1 y 5 segundos para simular el tiempo de ejecución de cada tarea
                    tiempoPrueba = (long) ((Math.random() * 5 + 1) * 1000);
                    // Se duerme el hilo inspector el tiempo asignado a cada tarea
                    Thread.sleep(tiempoPrueba);
                    // El hilo inspector le envía al hilo coche el la prueba a realizar
                    mensajeOut = String.format("%s realice la prueba: %s", nombreVehiculo, pruebas[i]);
// ENVIADO 5: Se envía mensaje al hilo coche
                    pw.println(mensajeOut);
                    
                    // El inspector recibe la mensajeOut del cliente y comprueba si se trata de una frase inadecuada
//RECIBIDO 1: Se recibe mensaje del hilo coche
                    mensajeIn = br.readLine();
                    System.out.println(String.format("Frase del %s: %s",nombreVehiculo, mensajeIn));
                    // Se implementa un bloque condicional para bajar la probabilidad de éxito para superar la prueba en caso de recibir una de las frases inadecuadas
                    if (comprobarElementoLista(mensajeIn, frasesInadecuadas)) {
                        probabilidad -= 10;
                    }
                    // Se implementa un bloque condicional para calcular el resultado de la prueba y comparar con la probabilidad de éxito
                    resultado = (int) (Math.random() * 100);
                    if (resultado < probabilidad) {
                        resultadoPrueba = "Si";
                        resultados[i] = resultadoPrueba;
                    } else {
                        resultadoPrueba = "No";
                        resultados[i] = resultadoPrueba;
                    }
                }

                // Una vez finalizadas las pruebas y calculado el resultado el hilo inspector lanza un mensaje de finalización
                mensajeOut = "Le retiro el walkie, esperé en la puerta. Gracias.";
//ENVIADO 6: Se envía mensaje al hilo coche
                pw.println(mensajeOut);
                
                // Mensaje de presentación de los resultados obtenidos
//ENVIADO 7: Se envía mensaje al hilo coche
                // Se implementa un bloque condicionar para lanzar uno u otro mensaje dependiendo del resultado obtenido
                if (!comprobarElementoLista("No", resultados)) {
                    pw.println("Prueba superada " + nombreVehiculo);
                    pw.println(nombreVehiculo+" "+mensajeFinal[0]);
                } else {
                    pw.println("Prueba no superada " + nombreVehiculo);
                    pw.println(nombreVehiculo+" "+mensajeFinal[1]);
                }

                // El hilo inspector libera la línea de inspección para ser reutilizada por un hilo coche que se encuentre esperando su turno
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
