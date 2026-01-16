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

/**
 * Clase Hilo Coche
 * 
 * @author Antonio Naranjo Castillo
 */
public class HiloCoche implements Runnable {

    // Declaración/iniciación de variables
    private static final String HOST = "localhost";
    private static final int PUERTO = 12349;
    private String nombreVehiculo;
    private String codigoTurno;
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
    private static final String[] frasesCorrectas = {
        "vale",
        "recibido",
        "entendido",
        "procedo",
        "hecho",
        "si",
        "correcto",
        "ok",
        "de acuerdo"
    };

    // Método constructor del HiloCoche (HiloCliente) El nombre será asignado a posteriori por parte del servidor principal
    public HiloCoche() {
        this.nombreVehiculo = null;
        this.codigoTurno=null;
    }

    // Método selector de frase aleatoria
    private String selectorFrases(String[] frases) {

        try {
            int elementoAleatorio = (int) (Math.random() * frases.length + 1);
            int indice = elementoAleatorio - 1;
            String frase = frases[indice];
            return frase;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error índice fuera de intervalo: " + e.getMessage());
            return null;
        }
    }

    // Método run del HiloCoche
    @Override
    public void run() {

        // Declaración de variables
        String mensajeIn;
        int numeroPruebas;
        int probabilidad;

        // Se generan los flujos de entrada y salida del hilo coche, comunicación servidor-cliente (hilo inspector - hilo coche)
        try {
            Socket s = new Socket(HOST, PUERTO);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);

//RECIBIDO 1
            // El vehículo recibe su nuevo nombre
            this.nombreVehiculo=br.readLine();
//RECIBIDO 2
            this.codigoTurno=br.readLine();
            System.out.println(String.format("Mi nuevo nombre es: %s y tiene asignado el turno nº %s", this.nombreVehiculo,this.codigoTurno));
//RECIBIDO 3  
            // El vehículo recibe el saludo de bienvenida del hilo inspector
            mensajeIn = br.readLine();
            System.out.println(String.format("Saludo del inspector al %s: %s", this.nombreVehiculo, mensajeIn));
//RECIBIDO 4         
            // El vehículo recibe el número de pruebas que va a realizar
            numeroPruebas = Integer.parseInt(br.readLine());
            System.out.println(String.format("Nº pruebas del %s: %d", this.nombreVehiculo,numeroPruebas));
            
            // Bucle for para el intercambio de flujo o conversación entre el hilo inspertor y el hilo coche
            for (int i = 0; i < numeroPruebas; i++) {
//RECIBIDO 5                
                // El hilo coche recibe la petición de cada prueba
                System.out.println(br.readLine());
                // El hilo coche responde al inspector con una frase pudiendo ser de las correctas o de las inadecuadas en función de una probabilidad del 70% a favor de las frases correctas
                probabilidad = (int) (Math.random() * 100);
//ENVIADO 1         
                // Se implementa un bloque condicional para decidir qué frase tipo de frase seleccionar de manera aleatoria y con una probabilidad del 70% para las frases correctas.
                if (probabilidad < 70) {
                    pw.println(selectorFrases(frasesCorrectas));
                } else {
                    pw.println(selectorFrases(frasesInadecuadas));
                }
            }
//RECIBIDO 6
            // El hilo coche recibe el mensaje de finalización de las pruebas
            System.out.println(br.readLine());
//RECIBIDO 7            
            // El hilo coche recibe el resultado de las pruebas
            System.out.println(br.readLine());
            System.out.println(br.readLine());

            // Cierre de recursos
            s.close();
            br.close();
            pw.close();

        } catch (IOException e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }

    }

}
