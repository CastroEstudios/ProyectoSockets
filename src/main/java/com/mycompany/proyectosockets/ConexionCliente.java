package com.mycompany.proyectosockets;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionCliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String args[]) throws SQLException {

        try {
            Socket socketServidor = new Socket(HOST, PUERTO);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socketServidor.getInputStream()));

            String saludos = entrada.readLine();
            System.out.println(saludos);
            System.out.println(entrada.readLine());
            
            if(saludos != null) {
                CRUDSockets c = new CRUDSockets();
                c.setVisible(true);
            }else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Servidor lleno. Intente conectarse más tarde.",
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
            socketServidor.close();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }
}
