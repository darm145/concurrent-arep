package edu.escuelaing.arep;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 *
 * @author David Ramirez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Web{

    /**
     *valor de la etiqueta
     * @return string con el valor de la etiqueta
     */
    String value();
}