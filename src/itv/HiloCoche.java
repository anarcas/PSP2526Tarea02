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
 *
 * @author anaranjo
 */
public class HiloCoche implements Runnable {

    // Declaración/iniciación de variables
    private static final String HOST = "localhost";
    private static final int PUERTO = 12349;
    private final String nombreVehiculo;
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
    private static final String MENSAJE_DESPEDIDA="FIN";

    // Método constructor del HiloCoche (HiloCliente)
    public HiloCoche(String nombreCoche) {
        this.nombreVehiculo = nombreCoche;
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

        String linea;
        int numeroPruebas;
        int probabilidad;

        try {
            Socket s = new Socket(HOST, PUERTO);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);

            // El vehículo le pasa su nombre al servidor principal
            pw.println(this.nombreVehiculo);
            
            // Una vez establecida la conexión con el hilo inspector el hilo coche le vuelve a pasar su nombre
            //pw.println(this.nombreVehiculo);
            
            // El vehículo recibe el saludo de bienvenida del servidor
            linea = br.readLine();
            System.out.println(linea);
            // El vehículo recibe el número de pruebas que va a realizar
            numeroPruebas = Integer.parseInt(br.readLine());
            // Bucle for para el intercambio de flujo entre inspertor y conductor
            for (int i = 0; i < numeroPruebas; i++) {
                // El conductor recibe la petición de cada prueba
                linea = br.readLine();
                System.out.println(linea);
                // El conductor responde al inspector
                probabilidad = (int) Math.random() * 100;
                if (probabilidad < 70) {
                    pw.println(selectorFrases(frasesCorrectas));
                } else {
                    pw.println(selectorFrases(frasesInadecuadas));
                }
            }

            // El conductor recibe el mensaje de terminación de las pruebas
            System.out.println(br.readLine());
            // El conductor recibe el resultado de las pruebas
            System.out.println(br.readLine());
            // El conductor avisa al inspector que se marcha
            pw.println(MENSAJE_DESPEDIDA);

            // Cierre de recursos
            s.close();
            br.close();
            pw.close();

        } catch (IOException e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }

    }

}
