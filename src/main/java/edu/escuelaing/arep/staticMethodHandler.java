package edu.escuelaing.arep;

import java.lang.reflect.Method;

/**
 *
 * @author estudiante
 */
public class staticMethodHandler implements Handler {
    private Method metodo;

    /**
     *
     * @param metodo
     */
    public staticMethodHandler(Method metodo){
        this.metodo=metodo;
    }

    /**
     *funcion para ejecutar un metodo sin parametros
     * @return string con el resultado del metodo ejecutado
     */
    public String procesar(){
        try{
            //metodos estaticos sin parametros 
             return (String) metodo.invoke(null,null);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        } 
    }

    /**
     *funcion para ejecutar un metodo con parametros
     * @param params lista de objetos con los parametros
     * @return string con el resultado del metodo ejecutado
     */
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