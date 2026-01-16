/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itv;

import java.net.Socket;

/**
 *
 * @author anaranjo
 */
public class TicketInspeccion {
    private Socket socket;
    private String nombreVehiculo;

    
    

    public TicketInspeccion(Socket s, String nombre) {
        this.socket = s;
        this.nombreVehiculo = nombre;
    }
    
    
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
