/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author 2dama
 */
public class Servidor {

    static final int PUERTO = 5000;
    int numConexiones = 0;
    ArrayList<Thread> listaHilos = new ArrayList();
    final Object lock = new Object();

    public Servidor() throws SQLException, IOException, InterruptedException {

        ServerSocket servidor = new ServerSocket(PUERTO);
        while(true) {
            Socket Cliente = servidor.accept();
            if (listaHilos.size() < 3) {
                Thread hilo = new Thread(new Cliente(Cliente));
                listaHilos.add(hilo);
                synchronized(lock) {
                    hilo.start();
                    System.out.println("Cliente conectando al servidor...");
                    Thread.sleep(3000);
                    System.out.println("Conectado!");
                }
                if (!hilo.isAlive()) {
                    listaHilos.remove(hilo);
                    numConexiones--;
                }
                numConexiones++;
            } else {
                // Reject connection
                Cliente.close();
                System.out.println("Server is full. Connection rejected.");
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        Servidor servidor = new Servidor();
    }
}

