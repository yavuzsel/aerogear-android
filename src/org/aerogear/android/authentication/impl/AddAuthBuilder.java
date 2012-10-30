/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication.impl;

import org.aerogear.android.Builder;
import org.aerogear.android.authentication.AuthenticationModule;

/**
 *
 */
public interface AddAuthBuilder<T extends AuthenticationModule> extends Builder<T> {
  
    AddAuthBuilder<T> enrollEndpoint(String enrollEndpoint);

    AddAuthBuilder<T> loginEndpoint(String loginEndpoint);

    AddAuthBuilder<T> logoutEndpoint(String logoutEndpoint);
    
    
    /**
     * Will build and add a value
     * 
     * @return 
     */
    public T add() ;

    
}
