/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itv;

import java.util.Scanner;

/**
 * Clase simulador de hilos coche encargada de simular la llegada de vehículos a
 * la estación. Genera múltiples hilos de tipo HiloCoche para representar el
 * flujo de clientes que solicitan una inspección técnica.
 *
 * @author Antonio Naranjo Castillo
 */
public class SimuladorCoches {

    /**
     * Punto de entrada para la generación de carga de trabajo. Instancia y
     * arranca los hilos de los vehículos, gestionando los retardos entre
     * llegadas para simular un flujo de tráfico realista.
     *
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here

        // Declaración de variables
        Scanner teclado = new Scanner(System.in);
        int numVehiculos;
        Thread hiloCoche;
        Thread[] hilosCoche;

        // Se implementa un bucle do-while que se ejecutará al menos una vez y siempre y cuando la variable de entrada por parte del usuario de la aplicación sea distinta de 0
        do {

            System.out.print("¿Cuántos coches llegan? ");
            numVehiculos = teclado.nextInt();
            hilosCoche = new Thread[numVehiculos];

            // Se implementa un bucle for para iniciar tantos hilos coche como indique el usuario
            for (int i = 0; i < numVehiculos; i++) {
                hiloCoche = new Thread(new HiloCoche());
                hilosCoche[i] = hiloCoche;
                hiloCoche.start();
            }

            // Los hilos se esperan antes de finalizar la simulación de los hilos coches
            for (Thread hilo : hilosCoche) {

                hilo.join();

            }

        } while (numVehiculos != 0);

        System.out.println(String.format("Itv's superadas: %d", HiloCoche.getPruebasSuperadas()));
        System.out.println(String.format("Itv's no superadas: %d", HiloCoche.getPruebasNoSuperadas()));

        // Cierre de recursos
        teclado.close();

    }

}
