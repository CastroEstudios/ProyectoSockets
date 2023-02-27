/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosockets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dama
 */
public class Cliente implements Runnable {

    Socket socket = null;
    PrintWriter salida;

    public Cliente(Socket socket) throws IOException {
        this.socket = socket;
        salida = new PrintWriter(socket.getOutputStream(), true);

    }

    @Override
    public void run() {
        salida.println("Hola " + socket.getInetAddress().getHostAddress());
        salida.println("Bienvenido al CRUD");

        try {
            System.out.println("Cerrando conexión...");
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
