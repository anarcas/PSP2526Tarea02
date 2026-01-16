/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase ServidorITV (Servidor principal de la estación ITV)
 * 
 * @author Antonio Naranjo Castillo
 */
public class ServidorITV {

    private static final int PUERTO = 12349;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Declaración/Iniciación de variables
        Thread hiloInspector;
        // Recurso compartido
        RecursoCompartidoITV rcITVinfierno = new RecursoCompartidoITV();
        // Ticket de recepción
        TicketInspeccion ti;
        // El servidor establecerá el nombre del coche
        String nombreVehiculo;
        int contadorVehiculos = 0;

        // Se crea un hilo inspector por cada línea disponible, solo y exclusivamente para que la CPU trabaje de la manera más eficiente posible
        for (int i = 0; i < RecursoCompartidoITV.NUM_LINEAS; i++) {
            hiloInspector = new Thread(new HiloInspector(rcITVinfierno));
            hiloInspector.start();
        }

        // El servidor quedará operativo 24/7 dentro de un bucle infinito esperando la llegada de un hilo coche que se conecte
        try (ServerSocket ss = new ServerSocket(PUERTO)) {
            System.out.println("Servidor ITV del Infierno arrancando...");
            while (true) {
                Socket s = ss.accept();
                
                // Una vez existe una conexión el servidor le asigna un nombre
                contadorVehiculos++;
                nombreVehiculo = String.format("Coche%d", contadorVehiculos);

                // Se genera el ticket de inspección pasando como argumento el objeto Socket y el String nombre del vehículo
                ti = new TicketInspeccion(s, nombreVehiculo);
                
                // Si existe una línea de inspección libre el hilo coche será atendido directamente por un hilo inspector en caso contrario esperará su turno
                if (rcITVinfierno.getLineasEnUso() < RecursoCompartidoITV.NUM_LINEAS) {
                    rcITVinfierno.atenderVehiculo(ti);
                } else {
                    // Si todas las lineas estan ocupadas entonces se almacena cada conexión que llegue a la estación ITV en la lista de espera 
                    rcITVinfierno.esperarTurno(ti);
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor E/S: " + e.getMessage());
        }
    }

}
