package edu.escuelaing.arep;

public class POJO {

    @Web("ejecutarClasePrueba")
    public static String ejecutar() {
        System.out.println("ejecuto el metodo desde el initializer");
        return "<html>" + "<head/>" + "<body>" + "<h2>  Metodo ejecutado desde web</h2>" + "</body>" + "</html>";
    }

    @Web("saludar")
    public static String saludar(String name) {
        return "<html>" + "<head/>" + "<body>" + "<h2> Hello " + name + "</h2>" + "</body>" + "</html>";
    }

    @Web("sumar")
    public static String sumar(String n1, String n2) {
        return "<html>" + "<head/>" + "<body>" + "<h2> La suma es: "
                + Integer.toString(Integer.parseInt(n1) + Integer.parseInt(n2)) + "</h2>" + "</body>" + "</html>";
    }
}
