/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itv;

import java.util.Scanner;

/**
 * Clase simulador de hilos coche
 * 
 * @author Antonio Naranjo Castillo
 */
public class SimuladorCoches {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here

        // Declaración de variables
        Scanner teclado;
        int numVehiculos;
        Thread hiloCoche;
        Thread[] hilosCoche;

        // Se implementa un bucle do-while que se ejecutará al menos una vez y siempre y cuando la variable de entrada por parte del usuario de la aplicación sea distinta de 0
        do {
            // Instanciación del objeto Scanner que almacenará el valor introducido por el usuario de la aplicación
//** Se gestionará la entrada de valores incorrectos números negativos o caracteres distintos de un dígito número entero
            teclado = new Scanner(System.in);
            System.out.print("¿Cuántos coches llegan? ");
            numVehiculos = teclado.nextInt();
            hilosCoche = new Thread[numVehiculos];

            // Se implementa un bucle for para iniciar tantos hilos coche como indique el usuario
            for (int i = 0; i < numVehiculos; i++) {
                hiloCoche = new Thread(new HiloCoche());
                hilosCoche[i]=hiloCoche;
                hiloCoche.start();
            }

            // Los hilos se esperan antes de finalizar la simulación de los hilos coches
            for (Thread hilo : hilosCoche) {

                    hilo.join();
     
            }

        } while (numVehiculos != 0);

        System.out.println(String.format("Itv's superadas: %d",HiloCoche.getPruebasSuperadas()));
        System.out.println(String.format("Itv's no superadas: %d",HiloCoche.getPruebasNoSuperadas()));
        
        // Cierre de recursos
        teclado.close();

    }

}
