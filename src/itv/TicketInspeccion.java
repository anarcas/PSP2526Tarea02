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
    private String codigoTurno;

    public TicketInspeccion(Socket s, String nombre, String numTurno) {
        this.socket = s;
        this.nombreVehiculo = nombre;
        this.codigoTurno = numTurno;
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

    public String getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(String codigo) {
        this.codigoTurno = codigo;
    }
}
