package edu.escuelaing.arep;

import java.lang.reflect.Method;

public class staticMethodHandler implements Handler {
    private Method metodo;
    public staticMethodHandler(Method metodo){
        this.metodo=metodo;
    }
    public String procesar(){
        try{
            //metodos estaticos sin parametros 
             return (String) metodo.invoke(null,null);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        } 
    }

    @Override
    public String procesarConParams(Object[] params) {
        try{
            //metodos estaticos sin parametros 
            System.out.println("paraaaaaaaams");
             return (String) metodo.invoke(null,params);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        } 
    }
    
}