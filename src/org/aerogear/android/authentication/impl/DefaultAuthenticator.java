/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication.impl;

import java.util.HashMap;
import java.util.Map;
import org.aerogear.android.Builder;
import org.aerogear.android.authentication.AuthenticationModule;
import org.aerogear.android.authentication.Authenticator;

/**
 *
 * @author summers
 */
public class DefaultAuthenticator implements Authenticator {

    Map<String, AuthenticationModule> modules = new HashMap<String, AuthenticationModule>();
    
    /**
     * %{@inheritDoc }
     */
    @Override
    public AuthenticationModule add(String name, Builder<? extends AuthenticationModule> builder) {
        modules.put(name, builder.build());
        return modules.get(name);
    }


    /**
     * %{@inheritDoc }
     */
    @Override
    public AuthenticationModule get(String name) {
        return modules.get(name);
    }

    /**
     * %{@inheritDoc }
     */
    @Override
    public AuthenticationModule remove(String name) {
        return modules.remove(name);
    }
    
}
