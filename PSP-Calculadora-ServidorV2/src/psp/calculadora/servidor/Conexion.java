package psp.calculadora.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Conexion {

    Calculo cal = new Calculo();

    Boolean condicionCierre = true;

    ServerSocket serverSocket;
    InetSocketAddress addr;
    Socket newSocket;

    /**
     * Constructor que abre la conexión y se pone a la escucha de con una
     * direccion y puerto por defecto y llama a la clase calculo que contiene el
     * metodo calculo
     */    
    public Conexion() {

        try {

            // System.out.println("Creando socket servidor");
            serverSocket = new ServerSocket();

            //System.out.println("Realizando el bind");
            String[] botones = {"Aceptar", "Por defecto", "Cancelar"};

            JPanel panel = new JPanel();
            panel.add(new JLabel("Escriba un puerto antes de pulsar aceptar: "));
            JTextField textField = new JTextField(10);
            panel.add(textField);

            // Función que habilita un JOptionPane con 3 botones y recoja el botón seleccionado
            int respuesta = JOptionPane.showOptionDialog(null, panel, "Introduzca un puerto",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, botones, null);

            // Switch que dependiendo de la respuesta, hace sus respectivas funciones
            switch (respuesta) {
                // En caso de darle al botón aceptar, recoge el puerto de la caja de texto
                case JOptionPane.YES_OPTION:
                    int intPuerto = Integer.parseInt(textField.getText());
                    addr = new InetSocketAddress("localhost", intPuerto);
                    //      System.out.println(addr);
                    break;
                // En este se le mete un puerto por defecto
                case JOptionPane.NO_OPTION:
                    addr = new InetSocketAddress("localhost", 5555);
                   
                    break;
                // Cierra el servidor
                default:
                    serverSocket.close();
                    System.exit(0);
                //      System.out.println("Terminado");
            }

            serverSocket.bind(addr);

            // Se hace el bucle do while para que el servidor quede a la escucha nuevas conexiónes con otro servidor
            do {

                //   System.out.println("Aceptando conexiones");
                newSocket = serverSocket.accept();

                //   System.out.println("Conexión recibida" + " " + newSocket);
                new hilo(newSocket).start();
            } while (true);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Se crea un nuevo hilo, cada nuevo cliente se conecte a la máquina
     */
    public class hilo extends Thread {

        Socket oldSocket;
        InputStream oldIs;
        OutputStream oldOs;
        byte[] mensaje;

        public hilo(Socket newSocket) throws IOException {

            oldSocket = newSocket;
           
        }

        @Override
        public void run() {

            try {
                oldIs = oldSocket.getInputStream();
                oldOs = oldSocket.getOutputStream();
                // Se hace un do while para quedar a la espera de nuevos calculos
                do {

                    mensaje = new byte[2000];

                    oldIs.read(mensaje);

                    oldOs.write(cal.calculo(new String(mensaje)).getBytes());
                } while (true);

                //newSocket.close();
                // System.out.println("Cerrando el socket servidor");
                // serverSocket.close();
                //System.out.println("Terminado");
            } catch (IOException e) {
                System.out.println(e);
            }

        }
    }

}
