/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication;

import java.net.URL;
import java.util.Map;
import org.aerogear.android.Callback;
import org.aerogear.android.core.HeaderAndBodyMap;
import org.aerogear.android.pipeline.Pipe;
import org.aerogear.android.pipeline.Type;

/**
 *
 * A module which can authenticate a user.  It also provides the 
 * necessary tools to log a user in, out, and modify requests from a 
 * %{@link Pipe} so they are seen as authenticated.             
 * 
 */
public interface AuthenticationModule {
    
    public Type getType();
    public URL getbaseURL();
    public String getLoginEndpoint();
    public String getLogoutEndpoint();
    public String getEnrollEndpoint();

    
    
    /**
     * 
     * Will try to register a user with a service using userData.
     * 
     * It will call the callbacks onSuccess with a parameter of a Map of the 
     * values returned by the enroll service or onFailure if there is an error
     * 
     * @param userData
     * @param callback 
     */
    public void enroll(Map<String, String> userData, Callback<HeaderAndBodyMap> callback);
    
    /**
     * 
     * Will try to log in a user using username and password.
     * 
     * It will call the callbacks onSuccess with a parameter of a Map of the 
     * values returned by the enroll service or onFailure if there is an error
     * 
     * @param username 
     * @param password 
     * @param callback 
     */
    public void login(String username, String password, Callback<HeaderAndBodyMap> callback);
    
    /**
     * 
     * Performs a logout of the current user.
     * 
     * It will call callback.onSuccess with no value on success and 
     * callback.onFailure if there is an error.
     * 
     * @param callback 
     */
    public void logout(Callback<Void> callback);
}
