/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itv;

import java.net.Socket;

/**
 * Clase TicketInspección que almacena tanto el socket del hilo coche que se
 * conectará con el hilo inspector y el nombre que el servidor principal le ha
 * asignado al hilo coche
 *
 * @author Antonio Naranjo Castillo
 */
public class TicketInspeccion {

    private Socket socket;
    private String nombreVehiculo;

    // Método constructor
    public TicketInspeccion(Socket s, String nombre) {
        this.socket = s;
        this.nombreVehiculo = nombre;
    }

    // Métodos getters y setters
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNombreVehiculo() {
        return nombreVehiculo;
    }

    public void setNombreVehiculo(String nombreVehiculo) {
        this.nombreVehiculo = nombreVehiculo;
    }
}
