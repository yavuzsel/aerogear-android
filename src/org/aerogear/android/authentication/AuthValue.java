/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this Annotation to mark in a {@link AuthenticationModule} a value to 
 * send to the server.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthValue {
    
    /**
     * This is the name of the value to send to the server.
     * It is assumed that auth tokens are sent as key/value pairs.
     * 
     * Ex Http Headers.
     */
    String name() default "";
}
