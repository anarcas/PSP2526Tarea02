/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itv;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author anaranjo
 */
public class RecursoCompartidoITV {

    // Lista de espera de los vehículos que esperan su turno para ser atendidos cuando exista alguna línea de inspección disponible
    public final Queue<Socket> listaEspera = new LinkedList<>();
    
    
    // Método para solicitar la entrada de un nuevo vehículo en la línea de inspección por parte del hilo inspector
    public synchronized Socket solicitarVehiculo() throws InterruptedException{
        // Si la lista de espera de hilos coches está vacía, el hilo inspector debe esperar hasta la llegada de un nuevo vehículo
        while (listaEspera.isEmpty()){
            wait();
        }
        // Si la lista no está vacía el primer vehículo en entrar será el primero en salir, empleando para ello el método poll() de la clase LinkedList
       return listaEspera.poll();    
    }
    
    
    // Método para almacenar en una lista de espera los vehículos pendientes de ser atendidos
    public synchronized void esperarTurno(Socket s, String nombre){
        // El Socket del coche que espera es almacenado en una lista de espera
        listaEspera.offer(s);
        // Se imprime en consola el nombre del coche que se encuentra esperando
        System.out.println(String.format("%s esperando para entrar.",nombre));
        // El hilo coche notifica a los hilos inspectores que se encuentra en la sala de espera esperando su turno para ser atendido
        notifyAll();
    }
    
    
    
    
    // Declaración/iniciación de variables
    public final int NUM_LINEAS=4;
    private int lineaEnUso = 0;
    
    // Método getter que devuelve el número de líneas en uso
    public int getLineasEnUso() {
        return lineaEnUso;
    }

    // Método para dar entrada a un vehículo para ser inspeccionado
    public synchronized void entradaVehiculo(){
        lineaEnUso++;
    }
    
    // Método para dar salida a un vehículo tras ser inspeccionado y notifica al siguiente que se encuentre esperando
    public synchronized void salidaVehiculo(){
        lineaEnUso--;
    }
}
