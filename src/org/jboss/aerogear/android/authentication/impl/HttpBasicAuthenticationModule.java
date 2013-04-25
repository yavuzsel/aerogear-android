/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.android.authentication.impl;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.PasswordAuthentication;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authentication.AbstractAuthenticationModule;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.authentication.AuthorizationFields;
import org.jboss.aerogear.android.http.HeaderAndBody;

import android.util.Base64;
import android.util.Pair;

/**
 *
 * @author summers
 */
public class HttpBasicAuthenticationModule extends AbstractAuthenticationModule {

    private final static String BASIC_HEADER = "Authorization";
    private final static String AUTHORIZATION_METHOD = "Basic";
    
    private final String loginEndpoint = "";
    private final String logoutEndpoint = "";
    private final String enrollEndpoint = "";
    private final URL baseURL;

    private boolean isLoggedIn = false;
    private PasswordAuthentication auth = new PasswordAuthentication("", new char[] {});
    
    public HttpBasicAuthenticationModule(URL baseURL, AuthenticationConfig config) {
        this.baseURL = baseURL;
        
    }
    
    @Override
    public URL getBaseURL() {
        return baseURL;
    }

    @Override
    public String getLoginEndpoint() {
        return loginEndpoint;
    }

    @Override
    public String getLogoutEndpoint() {
        return logoutEndpoint;
    }

    @Override
    public String getEnrollEndpoint() {
        return enrollEndpoint;
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public void login(String username, String password, final Callback<HeaderAndBody> callback) {
        isLoggedIn = true;
        auth = new PasswordAuthentication(username, password.toCharArray());
        THREAD_POOL_EXECUTOR.execute(new Runnable() {

            @Override
            public void run() {
                callback.onSuccess(new HeaderAndBody(new byte[]{}, new HashMap<String, Object>(1)));
            }
        });
        
    }

    @Override
    public void logout(final Callback<Void> callback) {
        clearPassword(auth.getPassword());
        auth = new PasswordAuthentication("", new char[] {});
        isLoggedIn = false;

        THREAD_POOL_EXECUTOR.execute(new Runnable() {

            @Override
            public void run() {
            	try {
					CookieStore store = ((CookieManager)CookieManager.getDefault()).getCookieStore();
					List<HttpCookie> cookies = store.get(baseURL.toURI());
					
					for (HttpCookie cookie : cookies) {
						store.remove(baseURL.toURI(), cookie);
					}
					
					callback.onSuccess((Void) null);
				} catch (URISyntaxException e) {
					callback.onFailure(e);
				}
                
            }
        });
        
        
    }

    
    
    @Override
    public AuthorizationFields getAuthorizationFields() {
        AuthorizationFields fields = new AuthorizationFields();
        List<Pair<String, String>> headerList = new ArrayList<Pair<String, String>>(1);
        headerList.add(new Pair<String, String>(BASIC_HEADER, getHashedAuth()));
        fields.setHeaders(headerList);
        return fields;
    }

    private String getHashedAuth() {
        StringBuilder headerValueBuilder = new StringBuilder(AUTHORIZATION_METHOD).append(" ");
        String unhashedCredentials = new StringBuilder(auth.getUserName()).append(":").append(auth.getPassword()).toString();
        String hashedCrentials = Base64.encodeToString(unhashedCredentials.getBytes(), Base64.DEFAULT);
        return headerValueBuilder.append(hashedCrentials).toString();
    }

    private void clearPassword(char[] password) {
        for (int i = 0; i < password.length; i++) {
            password[i] = '0';
        }
    }
    
}
