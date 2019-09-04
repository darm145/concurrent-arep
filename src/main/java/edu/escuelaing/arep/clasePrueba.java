package edu.escuelaing.arep;



public class clasePrueba {
    
    @Web(identifier="ejecutarClasePrueba")
    public static String  ejecutar() {
        System.out.println("ejecuto el metodo desde el initializer");
        return "asd";
    }
    
    @Web(identifier="ejecutarClasePrueba")
    public static String  ejecutar(String[] lista) {
        System.out.println("ejecuto el metodo desde el initializer");
        return "asd";
    }
}