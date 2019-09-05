package edu.escuelaing.arep;



public class clasePrueba {
    
    @Web("ejecutarClasePrueba")
    public static String  ejecutar() {
        System.out.println("ejecuto el metodo desde el initializer");
        return "<html>" + "<head/>" + "<body>" + "<h2>  Metodo ejecutado desde web</h2>"
        + "</body>"
        + "</html>";
    }
    
    
    public static String  ejecutar(String[] lista) {
        System.out.println("ejecuto el metodo desde el initializer");
        return "asd";
    }
    @Web("saludar")
    public static String saludar(String name){
        return"<html>" + "<head/>" + "<body>" + "<h2> Hello "+name+"</h2>"
        + "</body>"
        + "</html>";
    }
}