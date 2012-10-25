/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication;

import org.aerogear.android.Builder;

/**
 * This is a factory and manager for %{@link AuthenticationModule}
 * 
 *  It allows you to add and remove authentication modules.
 */
public interface Authenticator {
    
    /**
     * 
     * Creates a new %{@link AuthenticationModule } 
     * 
     * @param name a key to use to lookup the Module later
     * @param moduleBuilder a configured module to build and add to the 
     *        Authenticator
     * @return 
     */
    public AuthenticationModule add(String name, Builder<? extends AuthenticationModule> moduleBuilder);
   

    /**
     * Looks up and returns the AuthenticationModule if it exists.
     * 
     * If name isn't valid, return null.
     * 
     * @param name name of AuthorizationModule object
     * @return 
     */
    public AuthenticationModule get(String name);
    
     /**
     * Looks up and removes the AuthenticationModule if it exists.
     * 
     * @param name name of AuthorizationModule object
     * @return the AuthenticationModule which was removed; null if one was not found
     */
    public AuthenticationModule remove(String name);
    
    
}
