package edu.escuelaing.arep;

import java.lang.reflect.Method;

public class staticMethodHandler implements Handler {
    private Method metodo;
    public staticMethodHandler(Method metodo){
        this.metodo=metodo;
    }
    public void procesar(){
        try{
            //metodos estaticos sin parametros 
             metodo.invoke(null,null);
        }catch(Exception e){
            e.printStackTrace();
        }
       
       
        
    }
    
}