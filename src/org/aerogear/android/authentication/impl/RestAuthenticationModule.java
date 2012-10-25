/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication.impl;

import android.os.AsyncTask;
import com.google.gson.Gson;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aerogear.android.Callback;
import org.aerogear.android.authentication.AuthenticationModule;
import org.aerogear.android.core.HeaderAndBodyMap;
import org.aerogear.android.impl.core.HttpRestProvider;
import org.aerogear.android.impl.pipeline.Type;

/**
 *
 * @author summers
 */
public final class RestAuthenticationModule implements AuthenticationModule{
    public static final String TOKEN_HEADER = "Auth-Token";
    
    private final URL baseURL;

    private final static Gson gson = new Gson();
    
    private final String loginEndpoint;
    private final URL loginURL;
    
    private final String logoutEndpoint;
    private final URL logoutURL;
    
    private final String enrollEndpoint;
    private final URL enrollURL;
    
    private String authToken = "";
    private boolean isAuthenticated = false;
    
    private RestAuthenticationModule(URL baseURL, String loginEndpoint, String logoutEndpoint, String enrollEndpoint) throws MalformedURLException {
        this.baseURL = baseURL;
        this.loginEndpoint = loginEndpoint;
        this.logoutEndpoint = logoutEndpoint;
        this.enrollEndpoint = enrollEndpoint;
        
        this.loginURL = new URL(baseURL.toString() + loginEndpoint);
        this.logoutURL = new URL(baseURL.toString() + logoutEndpoint);
        this.enrollURL = new URL(baseURL.toString() + enrollEndpoint);
    }

    @Override
    public Type getType() {
        return Type.REST;
    }

    @Override
    public URL getbaseURL() {
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
    public void enroll(Map<String, String> userData, Callback<HeaderAndBodyMap> callback) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void login(final String username, final String password, final Callback<HeaderAndBodyMap> callback) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                HttpRestProvider provider = new HttpRestProvider(loginURL);
                String loginData = buildLoginData(username, password);
                try {
                    HeaderAndBodyMap result = provider.post(loginData);
                    authToken = result.get("Auth-Token");
                    isAuthenticated = true;
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure(e);
                }
                return null;
            }
        }.execute(null);
        
    }

    @Override
    public void logout(Callback<Void> callback) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAuthToken() {
        return authToken;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    

    private String buildLoginData(String username, String password) {
        int keyLength = "{'username': , 'password': } ".length();
        int stringLength = username.length() + keyLength + password.length();
        StringBuilder builder = new StringBuilder();
        
        builder.append("{")
                    .append("'username':").append(username)
                    .append(",'password':").append(password)
                .append("}");
        
        
        return builder.toString();
    }
 
    public static class Builder implements org.aerogear.android.Builder<RestAuthenticationModule> {
        private URL baseURL;
        private String loginEndpoint = "/auth/login";
        private String logoutEndpoint = "/auth/logout";
        private String enrollEndpoint = "/auth/enroll";

        public Builder() {
            try {
                baseURL = new URL("http://localhost:80");
            } catch (MalformedURLException ignore) {}
        }
        
        public Builder baseURL(URL baseURL) {
            this.baseURL = baseURL;
            return this;
        }

        public Builder loginEndpoint(String loginEndpoint) {
            this.loginEndpoint = loginEndpoint;
            return this;
        }

        public Builder logoutEndpoint(String logoutEndpoint) {
            this.logoutEndpoint = logoutEndpoint;
            return this;
        }

        public Builder enrollEndpoint(String enrollEndpoint) {
            this.enrollEndpoint = enrollEndpoint;
            return this;
        }
        
        /**
         * 
         * Instantiates a RestAuthenticationModule based on the defaults.
         * 
         * Defaults are:
         * 
         * private URL baseURL = new URL("http://localhost:80");
         * private String loginEndpoint = "/login";
         * private String logoutEndpoint = "/logout";
         * private String enrollEndpoint = "/enroll";
         * 
         * 
         * @return 
         * @throws IllegalArgumentException if baseURL + anyEndpoint is not a
         * valid URL
         */
        @Override
        public RestAuthenticationModule build() {
            try {
                return new RestAuthenticationModule(this.baseURL, 
                                                    this.loginEndpoint, 
                                                    this.logoutEndpoint,
                                                    this.enrollEndpoint);
            } catch (MalformedURLException ex) {
                Logger.getLogger(RestAuthenticationModule.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex);
            }
        }

    }
    
}
