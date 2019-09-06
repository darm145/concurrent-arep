package edu.escuelaing.arep;

/**
 *
 * @author David Ramirez
 */
public interface Handler{

    /**
     *funcion para ejecutar un metodo sin parametros
     * @return string con el resultado del metodo ejecutado
     */
    public String procesar();

    /**
     *funcion para ejecutar un metodo con parametros
     * @param params lista de objetos con los parametros
     * @return string con el resultado del metodo ejecutado
     */
    public String procesarConParams(Object[] params);
}