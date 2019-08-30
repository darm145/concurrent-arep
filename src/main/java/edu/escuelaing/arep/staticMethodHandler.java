package edu.escuelaing.arep;

import java.lang.reflect.Method;

public class staticMethodHandler implements Handler {
    private Method metodo;
    public void procesar(Method metodo){
        this.metodo=metodo;
        
    }
    
}