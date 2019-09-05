package edu.escuelaing.arep;

import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
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
            String request = "";
            String line;
            try {
                while (!(line = in.readLine()).equals("")) {
                    request += line + "\n";
                    
                }
            } catch (NullPointerException e) {
                out.print("HTTP/1.1 404 not Found \r\n");

            }
            System.out.println(request);
            handleRequest(request,out,clientSocket.getOutputStream());
            in.close();

        }
        

    }

    public static void inicializar(String route) {
        try {
            Class<?> c = Class.forName(route);
            for (Method metodo : c.getMethods()) {
                if (metodo.isAnnotationPresent(Web.class)) {
                    Handler h = new staticMethodHandler(metodo);
                    listaURLHandler
                            .put("/app/" + c.getSimpleName() + "/" + metodo.getAnnotation(Web.class).identifier(), h);
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

    private static void handleRequest(String request, PrintWriter out,OutputStream clientOutput) throws IOException{
        try{
            String[] parts = request.trim().split("\n");
            String route =parts[0].split(" ")[1];
            String[] elements=route.split("/");
            String element=elements[elements.length-1];
            if(element.endsWith(".jpg")){
                BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") +"/recursos/imagenes/"+ element));
                ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
                DataOutputStream writeimg=new DataOutputStream(clientOutput);
                ImageIO.write(image, "jpg",ArrBytes);
                writeimg.writeBytes("HTTP/1.1 200 OK \r\n");
                writeimg.writeBytes("Content-Type: image/jpg \r\n");
                writeimg.writeBytes("Content-Length: "+ArrBytes.toByteArray().length+"\r\n");
                writeimg.writeBytes("\r\n");
                writeimg.write(ArrBytes.toByteArray());
                System.out.println(System.getProperty("user.dir") +"\\recursos\\imagenes\\"+ element);
            }
            else{
                out.print("HTTP/1.1 200 OK \r\n");
                out.print("Content-Type: text/html \r\n");
                out.print("\r\n");
                out.print("<html>" + "<head/>" + "<body>" + "<h2>  NADAAAAAAAA</h2>" + "</body>" + "</html>");
            }
            out.close();
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("error en consulta");
        }
        
        
    }
}
