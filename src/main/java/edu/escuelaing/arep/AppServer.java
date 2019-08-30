package edu.escuelaing.arep;

import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Method;    

public class AppServer {
    private HashMap<String,Handler> listaURLHandler=new HashMap<String,Handler>();
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4567);
        } catch (IOException e) {
            System.err.println("Could not listen on port");
            System.exit(1);
        }
        inicializar();
        Socket clientSocket = null;
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
            inicializar();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.close();
        in.close();
    }

    public static void inicializar(){
        try{
            Class<?> c =Class.forName("edu.escuelaing.arep.clasePrueba");
            //Class[] argTypes = new Class[] { String[].class };
            Method execute = c.getDeclaredMethod("ejecutar", null);

            System.out.format("invoking %s.ejecutar()%n", c.getName());
            execute.invoke(null,null);

        }
        
        catch (Exception x) {   
            x.printStackTrace();}
        
    }
}    



