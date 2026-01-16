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
 * Clase Hilo Inspector Clase que representa a un inspector de la estación. Se
 * encarga de procesar los vehículos de la cola y realizar las inspecciones.
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
    private static final String[] condicional = {
        "SI",
        "NO"
    };

    // Método constructor del HiloInspector
    public HiloInspector(RecursoCompartidoITV rcITVinfierno) {
        this.rcITV = rcITVinfierno;
    }

    // Método para acceder al objeto Socket con el cual se establecerá la conexión con el hilo cliente
    public Socket getSocket() {
        return s;
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

    // Método run del HiloInspector donde se implementa su lógica
    @Override
    public void run() {

        // Declaración de variables
        String nombreVehiculo;
        String codigoTurno;
        String mensajeOut;
        String mensajeOutConsola;
        String mensajeIn;
        String numeroPruebas;
        long tiempoPrueba;
        int probabilidad;
        int resultado;
        String resultadoPrueba;
        String[] resultados;
        String[] frasesRecibidas;
        int[] puntuaciones;
        int[] probabilidades;
        TicketInspeccion ti;

        // Se implementa un bucle infinito while para que los hilos inspectores no mueran nunca, estación ITV 24/7 operativa.
        while (true) {
            // Se inician/instancian las variables
            resultados = new String[pruebas.length];
            frasesRecibidas = new String[pruebas.length];
            puntuaciones = new int[pruebas.length];
            probabilidades = new int[pruebas.length];
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
                codigoTurno = String.valueOf(RecursoCompartidoITV.codigoTurno++);
                //System.out.println(String.format("%s entra en la ITV.", nombreVehiculo));
//ENVIADO 1: Se envía mensaje al hilo coche
                pw.println(nombreVehiculo);
//ENVIADO 2: Se envía mensaje al hilo coche
                pw.println(codigoTurno);

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
                    //System.out.println(String.format("Frase del %s: %s",nombreVehiculo, mensajeIn));
                    // Se implementa un bloque condicional para bajar la probabilidad de éxito para superar la prueba en caso de recibir una de las frases inadecuadas
                    if (comprobarElementoLista(mensajeIn, frasesInadecuadas)) {
                        probabilidad -= 10;
                    }
                    // Se implementa un bloque condicional para calcular el resultado de la prueba y comparar con la probabilidad de éxito
                    resultado = (int) (Math.random() * 100);
                    if (resultado < probabilidad) {
                        resultadoPrueba = "Si";
                        resultados[i] = resultadoPrueba;
                        frasesRecibidas[i] = mensajeIn;
                        puntuaciones[i] = resultado;
                        probabilidades[i] = probabilidad;
                    } else {
                        resultadoPrueba = "No";
                        resultados[i] = resultadoPrueba;
                        frasesRecibidas[i] = mensajeIn;
                        puntuaciones[i] = resultado;
                        probabilidades[i] = probabilidad;
                    }
                }

                // Una vez finalizadas las pruebas y calculado el resultado el hilo inspector lanza un mensaje de finalización
                mensajeOut = "Le retiro el walkie, esperé en la puerta. Gracias.";
//ENVIADO 6: Se envía mensaje al hilo coche
                pw.println(mensajeOut);
                
                // Mensaje de presentación de los resultados obtenidos
//ENVIADO 7: Se envía mensaje al hilo coche
//ENVIADO 8: Se envía mensaje al hilo coche
//ENVIADO 9: Se envía mensaje al hilo coche
                // Se implementa un bloque condicionar para lanzar uno u otro mensaje dependiendo del resultado obtenido
                System.out.println(String.format("%n%s Resultado %s %s", "-".repeat(14), nombreVehiculo, "-".repeat(14)));
                if (!comprobarElementoLista("No", resultados)) {
                    mensajeOutConsola = String.format("%s ITV SUPERADA.", nombreVehiculo);
                    pw.println(mensajeOutConsola);
                    mensajeOut = condicional[0];
                    pw.println(mensajeOut);
                    mensajeOut = String.format("%s %s", nombreVehiculo, mensajeFinal[0]);
                    pw.println(mensajeOut);
                } else {
                    mensajeOutConsola = String.format("%s ITV NO SUPERADA.", nombreVehiculo);
                    pw.println(mensajeOutConsola);
                    mensajeOut = condicional[1];
                    pw.println(mensajeOut);
                    mensajeOut = String.format("%s %s", nombreVehiculo, mensajeFinal[1]);
                    pw.println(mensajeOut);
                }

                System.out.println(mensajeOutConsola);
                // Salida de resultados
                for (int i = 0; i < pruebas.length; i++) {
                    System.out.println(String.format("%s: %s (\"%s\" - prob %d%%)", pruebas[i], resultados[i], frasesRecibidas[i], probabilidades[i]));
                    //System.out.println(String.format("%s: %s (\"%s\" - prob %d%% Resultado=%d)", pruebas[i], resultados[i], frasesRecibidas[i], probabilidades[i],puntuaciones[i]));
                }
                System.out.println(String.format("%s%n", "-".repeat(46)));

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
