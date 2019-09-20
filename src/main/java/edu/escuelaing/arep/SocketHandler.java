package edu.escuelaing.arep;

import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;

/**
 *
 * @author David Ramirez
 */
public class SocketHandler implements Runnable {
    private static HashMap<String, Handler> listaURLHandler = new HashMap<String, Handler>();
    Socket clientSocket;

    public SocketHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void receiveRequest() throws IOException {
        System.out.println("hilooooooooooooooooooooOO"+Thread.currentThread().getId());

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
       
        handleRequest(request, out, clientSocket.getOutputStream());
        in.close();

    }

    /**
     * Metodo que permite guardar los metodos web de un POJO para que sean
     * accesibles mas facilmente
     * 
     * @param route la direccion del POJO
     */
    public static void inicializar(String route) throws Exception {

        Class<?> c = Class.forName(route);
        for (Method metodo : c.getMethods()) {
            if (metodo.isAnnotationPresent(Web.class)) {
              
                Handler h = new staticMethodHandler(metodo);
                listaURLHandler.put("/app/" + c.getSimpleName() + "/" + metodo.getAnnotation(Web.class).value(), h);
            }

        }
        // Class[] argTypes = new Class[] { String[].class };

    }

    /**
     * Metodo que Desglosa una solicitud hecha por el browser y llama las funciones
     * segun sea el caso
     * 
     * @param request      la solicitud hecha por el browser
     * @param out          un writer que permite escribir archivos sencillos como
     *                     texto al ususario
     * @param clientOutput stream que permite escribir archivos complejos como
     *                     imagenes al usuario
     */
    private static void handleRequest(String request, PrintWriter out, OutputStream clientOutput) throws IOException {
        try {
            String[] parts = request.trim().split("\n");
            String route = parts[0].split(" ")[1];
            String[] elements = route.split("/");

            if (elements.length == 0) {
                handleHtml("index.html", out, clientOutput);
            } else {
                String element = elements[elements.length - 1];

                if (element.endsWith(".jpg")) {
                    handleImage(element, clientOutput, out);
                } else if (element.endsWith(".html")) {
                    handleHtml(element, out, clientOutput);
                } else if (route.contains("/app")) {
                    handleMethod(elements, out, clientOutput);
                } else {
                    Error404(clientOutput);
                }

            }

            out.close();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("error en consulta");
        }

    }

    /**
     * funcion para manejar metodos recibidos en la URL bajo la
     * secuencia/app/{clase}/{metodo}
     * 
     * @param elements     lista que contiene los elementos de la URL separados por
     *                     "/"
     * @param out          un writer que permite escribir archivos sencillos como
     *                     texto al ususario
     * @param clientOutput stream que permite escribir archivos complejos como
     *                     imagenes al usuario
     */
    private static void handleMethod(String[] elements, PrintWriter out, OutputStream clientOutput) throws IOException {
        if (elements.length == 4) {

            String clase = elements[2];
            String metodo = elements[3];
            String key = "/app/" + clase + "/" + metodo;

            if (!listaURLHandler.containsKey(key)) {
                try {

                    inicializar("edu.escuelaing.arep." + clase);

                } catch (Exception e) {
                    Error404(clientOutput);
                    return;
                }
            }
            try {
                if (!elements[3].contains("?")) {
                   
                    String result;
                    result = listaURLHandler.get(key).procesar();
                    out.print("HTTP/1.1 200 OK \r\n");
                    out.print("Content-Type: text/html \r\n");
                    out.print("\r\n");
                    out.print(result);
                } else {
                    key = key.split("\\?")[0];
                    String paramString = elements[3].split("\\?")[1];
                    String[] paramValues = paramString.split("&");
                    Object[] params = new Object[paramValues.length];
                    for (int i = 0; i < paramValues.length; i++) {
                        params[i] = paramValues[i].split("=")[1];
                    }
                   
                    String result = listaURLHandler.get(key).procesarConParams(params);
                    out.print("HTTP/1.1 200 OK \r\n");
                    out.print("Content-Type: text/html \r\n");
                    out.print("\r\n");
                    out.print(result);

                }

            } catch (Exception e) {
                Error404(clientOutput);
            }

        } else {
            Error404(clientOutput);
        }
    }

    /**
     * funcion para manejar los archivos HTML recibidos en la URL
     * 
     * @param element      nombre del archivo html a buscar
     * @param out          un writer que permite escribir archivos sencillos como
     *                     texto al ususario
     * @param clientOutput stream que permite escribir archivos complejos como
     *                     imagenes al usuario
     */

    private static void handleHtml(String element, PrintWriter out, OutputStream clientOutput) throws IOException {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(System.getProperty("user.dir") + "/recursos/html/" + element));
            out.print("HTTP/1.1 200 OK \r\n");
            out.print("Content-Type: text/html \r\n");
            out.print("\r\n");
            String line;
            while ((line = br.readLine()) != null) {
                out.print(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            Error404(clientOutput);
        }

    }

    /**
     * funcion para manejar imagenes recibidos en la url
     * 
     * @param element      nombre de la imagen a buscar
     * @param out          un writer que permite escribir archivos sencillos como
     *                     texto al ususario
     * @param clientOutput stream que permite escribir archivos complejos como
     *                     imagenes al usuario
     */

    private static void handleImage(String element, OutputStream clientOutput, PrintWriter out) throws IOException {
        try {
            BufferedImage image = ImageIO
                    .read(new File(System.getProperty("user.dir") + "/recursos/imagenes/" + element));
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream writeimg = new DataOutputStream(clientOutput);
            ImageIO.write(image, "jpg", ArrBytes);
            writeimg.writeBytes("HTTP/1.1 200 OK \r\n");
            writeimg.writeBytes("Content-Type: image/jpg \r\n");
            writeimg.writeBytes("Content-Length: " + ArrBytes.toByteArray().length + "\r\n");
            writeimg.writeBytes("\r\n");
            writeimg.write(ArrBytes.toByteArray());
       
        } catch (IOException e) {
            Error404(clientOutput);
        }

    }

    /**
     * funcion para mostrar un Error 404 en caso de que algun fichero o metodo no
     * sea encontrado
     * 
     * @param clientOutput stream que permite escribir archivos complejos como
     *                     imagenes al usuario
     */

    private static void Error404(OutputStream clientOutput) throws IOException {
        BufferedImage image = ImageIO
                .read(new File(System.getProperty("user.dir") + "/recursos/imagenes/404error.jpg"));
        ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
        DataOutputStream writeimg = new DataOutputStream(clientOutput);
        ImageIO.write(image, "jpg", ArrBytes);
        writeimg.writeBytes("HTTP/1.1 200 OK \r\n");
        writeimg.writeBytes("Content-Type: text/html \r\n");
        // writeimg.writeBytes("Content-Length: " + ArrBytes.toByteArray().length +
        // "\r\n");
        writeimg.writeBytes("\r\n");
        writeimg.writeBytes("<html>" + "<head/>" + "<body>" + "<h2>  ERROR, ARCHIVO NO ENCONTRADO</h2>"
                + "<img src= \" " + System.getProperty("user.dir") + "\\recursos\\imagenes\\404error.jpg\">" + "</body>"
                + "</html>");

    }

    @Override
    public void run() {
        try {
            receiveRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
