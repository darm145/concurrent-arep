package edu.escuelaing.arep;

import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.escuelaing.arep.SocketHandler;

public class Balancer {

    private static ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;

            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                SocketHandler sh = new SocketHandler(clientSocket);
                pool.submit(sh);

            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

        }
    }

    /**
     * funcion para obtener un puerto por el cual el servidor va a trabajar
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
        // returns default port if heroku-port isn't set (i.e. on localhost)

    }
}