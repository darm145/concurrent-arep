package edu.escuelaing.arep;

import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Method;

public class AppServer {
    private static HashMap<String, Handler> listaURLHandler = new HashMap<String, Handler>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4567);
        } catch (IOException e) {
            System.err.println("Could not listen on port");
            System.exit(1);
        }
        inicializar("edu.escuelaing.arep.clasePrueba");
        while (true) {
            Socket clientSocket = null;
            
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();

            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request="";
            String line;
            try{
                while(!(line=in.readLine()).equals("") ){
                    request+=line+"\n";
                    line=in.readLine();
                }
            }catch(NullPointerException e){
                out.print("HTTP/1.1 404 not Found \r\n");

            }
            
            System.out.println(request);
            out.print("HTTP/1.1 200 OK \r\n");
            out.print("Content-Type: text/html \r\n");
            out.print("\r\n");
            out.print("<html>"+
            "<head/>"+
            "<body>"+
            "<h2>  HOLAAAAAAAAAAAAAAAAAAAAAAAA</h2>"+
            "</body>"+
            "</html>");
            out.close();
            in.close();
            clientSocket.close();
            
            

        }
      

    }

    public static void inicializar(String route) {
        try {
            Class<?> c = Class.forName(route);
            for(Method metodo: c.getMethods()){
                if(metodo.isAnnotationPresent(Web.class)){
                    Handler h= new staticMethodHandler(metodo);
                    System.out.println(c.getCanonicalName()+"       "+c.getName()+"            "+c.getSimpleName() );
                    listaURLHandler.put("/app/"+c.getName()+"/"+metodo.getAnnotation(Web.class).identifier(),h);
                }
                
                
            }
            // Class[] argTypes = new Class[] { String[].class };
            Method execute = c.getDeclaredMethod("ejecutar", null);

            System.out.format("invoking %s.ejecutar()%n", c.getName());
            execute.invoke(null, null);

        }

        catch (Exception x) {
            x.printStackTrace();
        }

    }
}
