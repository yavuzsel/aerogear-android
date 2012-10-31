/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aerogear.android.authentication.impl;

import org.aerogear.android.authentication.AddAuthBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aerogear.android.Callback;
import org.aerogear.android.authentication.AuthenticationModule;
import org.aerogear.android.core.HeaderAndBody;
import org.aerogear.android.impl.core.HttpRestProvider;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.aerogear.android.authentication.AuthValue;
import org.aerogear.android.core.HttpProvider;

/**
 * A module for authenticating with restful AG services.
 */
public final class RestAuthenticationModule implements AuthenticationModule {
    
    private final URL baseURL;

    private final static Gson gson = new Gson();
    
    private final String loginEndpoint;
    private final URL loginURL;
    
    private final String logoutEndpoint;
    private final URL logoutURL;
    
    private final String enrollEndpoint;
    private final URL enrollURL;

        
    /**
     * This is the field which stores the AG security token.
     */
    private String authToken = "";
    
    /**
     * This is the name of the header to set for the token.
     */
    private final String tokenHeaderName;
    
    private boolean isLoggedIn = false;

    private RestAuthenticationModule(URL baseURL, String loginEndpoint, String logoutEndpoint, String enrollEndpoint, String tokenHeaderName) throws MalformedURLException {
        this.baseURL = baseURL;
        this.loginEndpoint = loginEndpoint;
        this.logoutEndpoint = logoutEndpoint;
        this.enrollEndpoint = enrollEndpoint;
        this.tokenHeaderName = tokenHeaderName;
        
        this.loginURL = new URL(baseURL.toString() + loginEndpoint);
        this.logoutURL = new URL(baseURL.toString() + logoutEndpoint);
        this.enrollURL = new URL(baseURL.toString() + enrollEndpoint);
        
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
    public void enroll(final Map<String, String> userData,final Callback<HeaderAndBody> callback) {
        new AsyncTask<Void, Void, Void>() {

            HeaderAndBody result = null;
            Exception exception = null;
            
            @Override
            protected Void doInBackground(Void... params) {
                HttpRestProvider provider = new HttpRestProvider(enrollURL);
                String enrollData = new JSONObject(userData).toString();
                try {
                    result = provider.post(enrollData);
                    authToken = result.getHeader(tokenHeaderName).toString();
                    isLoggedIn = true;
                    
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void ignore) {
                super.onPostExecute(ignore);
                if (exception == null) {
                    callback.onSuccess(this.result);
                } else {
                    callback.onFailure(exception);
                }
            }
            
            
            
        }.execute(null);    
    }

    @Override
    public void login(final String username, final String password, final Callback<HeaderAndBody> callback) {
        new AsyncTask<Void, Void, Void>() {
            private Exception exception;
            private HeaderAndBody result;

            @Override
            protected Void doInBackground(Void... params) {
                HttpRestProvider provider = new HttpRestProvider(loginURL);
                String loginData = buildLoginData(username, password);
                try {
                    result = provider.post(loginData);
                    authToken = result.getHeader(tokenHeaderName).toString();
                    isLoggedIn = true;
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }
            
            @Override
            protected void onPostExecute(Void ignore) {
                super.onPostExecute(ignore);
                if (exception == null) {
                    callback.onSuccess(this.result);
                } else {
                    callback.onFailure(exception);
                }
            }
            
        }.execute(null);
        
    }

    @Override
    public void logout(final Callback<Void> callback) {
        new AsyncTask<Void, Void, Void>() {
            private Exception exception;

            @Override
            protected Void doInBackground(Void... params) {
                HttpRestProvider provider = new HttpRestProvider(logoutURL);
                try {
                    provider.post("");
                    authToken = "";
                    isLoggedIn = false;
                    
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }
            
              @Override
            protected void onPostExecute(Void ignore) {
                super.onPostExecute(ignore);
                if (exception == null) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(exception);
                }
            }
            
        }.execute(null);    
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    protected String getAuthToken() {
        return authToken;
    }

    private String buildLoginData(String username, String password) {
    	
    	
    	JsonObject response = new JsonObject();
    	response.addProperty("username", username);
    	response.addProperty("password", password);
    	return response.toString();
    	
    }

    @Override
    public void applyAuthentication(HttpProvider httpProvider) {
        httpProvider.setDefaultHeader(tokenHeaderName, authToken);
    }
 
    /**
     * This class extended by {@link DefaultAuthenticator#auth(org.aerogear.android.authentication.AuthType, java.net.URL) }
     * to creating an adding builder
     */
    protected static abstract class Builder implements AddAuthBuilder<RestAuthenticationModule> {
        private final URL baseURL;
        private       String tokenHeaderName = "Auth-Token";
        private       String loginEndpoint   = "/auth/login";
        private       String logoutEndpoint  = "/auth/logout";
        private       String enrollEndpoint  = "/auth/enroll";

        public Builder(URL baseURL) {
            this.baseURL = baseURL;
        }

        public AddAuthBuilder tokenHeader(String tokenHeaderName) {
            this.tokenHeaderName = tokenHeaderName;
            return this;
        }

        
        @Override
        public AddAuthBuilder loginEndpoint(String loginEndpoint) {
            this.loginEndpoint = loginEndpoint;
            return this;
        }

        @Override
        public AddAuthBuilder logoutEndpoint(String logoutEndpoint) {
            this.logoutEndpoint = logoutEndpoint;
            return this;
        }

        @Override
        public AddAuthBuilder enrollEndpoint(String enrollEndpoint) {
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
                                                    this.enrollEndpoint,
                                                    this.tokenHeaderName);
            } catch (MalformedURLException ex) {
                Logger.getLogger(RestAuthenticationModule.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex);
            }
        }


    }
    
}
