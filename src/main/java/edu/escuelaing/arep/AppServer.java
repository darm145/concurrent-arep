package edu.escuelaing.arep;

import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;

public class AppServer {
    private static HashMap<String, Handler> listaURLHandler = new HashMap<String, Handler>();

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
            handleRequest(request, out, clientSocket.getOutputStream());
            in.close();

        }

    }

    public static void inicializar(String route) throws Exception {

        Class<?> c = Class.forName(route);
        for (Method metodo : c.getMethods()) {
            if (metodo.isAnnotationPresent(Web.class)) {
                System.out.println("entra2");
                Handler h = new staticMethodHandler(metodo);
                listaURLHandler.put("/app/" + c.getSimpleName() + "/" + metodo.getAnnotation(Web.class).value(), h);
            }

        }
        // Class[] argTypes = new Class[] { String[].class };
        Method execute = c.getDeclaredMethod("ejecutar", null);

        System.out.format("invoking %s.ejecutar()%n", c.getName());
        execute.invoke(null, null);

    }

    private static void handleRequest(String request, PrintWriter out, OutputStream clientOutput) throws IOException {
        try {
            String[] parts = request.trim().split("\n");
            String route = parts[0].split(" ")[1];
            String[] elements = route.split("/");
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
            out.close();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("error en consulta");
        }

    }

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
                    key=key.split("\\?")[0];
                    String paramString = elements[3].split("\\?")[1];
                    String[] paramValues = paramString.split("&");
                    Object[] params = new Object[paramValues.length];
                    for (int i = 0; i < paramValues.length; i++) {
                        params[i] = paramValues[i].split("=")[1];
                    }
                    System.out.println(key);
                    String result=listaURLHandler.get(key).procesarConParams(params);
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
            System.out.println(System.getProperty("user.dir") + "\\recursos\\imagenes\\" + element);
        } catch (IOException e) {
            Error404(clientOutput);
        }

    }

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

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
        // returns default port if heroku-port isn't set (i.e. on localhost)

    }
}
