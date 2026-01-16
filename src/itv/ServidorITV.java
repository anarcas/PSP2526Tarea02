 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author anaranjo
 */
public class ServidorITV {

    private static final int PUERTO = 12349;
    private static final int NUM_LINEAS = 4;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Declaración/Iniciación de variables
        Thread hiloInspector;
        // Recurso compartido
        RecursoCompartidoITV rcITVinfierno = new RecursoCompartidoITV();
        // Mensaje recibido por parte del hilo coche que establece la conexión
        String nombreCoche;

        // Se crean tantos hilos inspectores como líneas disponga la estación ITV
        for (int i = 0; i < NUM_LINEAS; i++) {
            hiloInspector = new Thread(new HiloInspector(rcITVinfierno));
            hiloInspector.start();
        }

        // El servidor quedará operativo 24/7 dentro de un bucle infinito esperando la llegada de un hilo coche que se conecte
        try (ServerSocket ss = new ServerSocket(PUERTO)) {
            System.out.println("Servidor ITV del Infierno arrancando...");
            while (true) {
                Socket s = ss.accept();
                // El hilo coche le envía su nombre al servidor principal
                // Se generan los flujos de entrada y salida, comunicación servidor-cliente (hilo servidor principal - hilo coche)
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                nombreCoche=br.readLine();
                
                // Se almacena en la lista de espera cada hilo coche que llegue a la estación ITV
                rcITVinfierno.esperarTurno(s,nombreCoche);
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor E/S: " + e.getMessage());
        }
    }

}
