package edu.escuelaing.arep;

/**
 *
 * @author estudiante
 */
public class POJO {

    /**
     *metodo sin parametro 
     * @return html con una respuesta
     */
    @Web("ejecutarClasePrueba")
    public static String ejecutar() {
        System.out.println("ejecuto el metodo desde el initializer");
        Double a= new Double(2);
        return a.toString();
    }

    /**
     *metodo con parametros para saludar al usuario
     * @param name nombre usuario a saludar
     * @return string del html saludando al usuario
     */
    @Web("saludar")
    public static String saludar(String name) {
        return "<html>" + "<head/>" + "<body>" + "<h2> Hello " + name + "</h2>" + "</body>" + "</html>";
    }

    /**
     *metodo con parametros para sumar 2 numeros
     * @param n1 numero 1 a operar
     * @param n2 numero 2 a operar
     * @return html con el resultado de la suma
     */
    @Web("sumar")
    public static String sumar(String n1, String n2) {
        return "<html>" + "<head/>" + "<body>" + "<h2> La suma es: "
                + Integer.toString(Integer.parseInt(n1) + Integer.parseInt(n2)) + "</h2>" + "</body>" + "</html>";
    }

}
