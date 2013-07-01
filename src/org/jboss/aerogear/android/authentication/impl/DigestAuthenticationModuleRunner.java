/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jboss.aerogear.android.authentication.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpException;
import org.jboss.aerogear.android.http.HttpProvider;
import org.json.JSONObject;

public class DigestAuthenticationModuleRunner extends AbstractAuthenticationModuleRunner {

    private static String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";
    
    private static final String REALM = "realm";
    private static final String DOMAIN = "domain";
    private static final String NONCE = "nonce";
    private static final String STALE = "stale";
    private static final String ALGORITHM = "algorithm";
    private static final String QOP_OPTIONS = "qop";
    private static final String OPAQUE = "opaque";
    
    private String cnonce = UUID.randomUUID().toString();
    private int nonce_count = 0;
    private String nonce = "";
    private String qop = "";
    private String realm;
    private String domain;
    private String algorithm;
    private String stale;
    private String opaque;
    
    
    /**
     * @param baseURL
     * @param config
     * @throws IllegalArgumentException if an endpoint can not be appended to
     * baseURL
     */
    public DigestAuthenticationModuleRunner(URL baseURL, AuthenticationConfig config) {
        super(baseURL, config);
    }
    
    String buildLoginData(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public HeaderAndBody onEnroll(final Map<String, String> userData) {
        HttpProvider provider = httpProviderFactory.get(enrollURL, timeout);
        String enrollData = new JSONObject(userData).toString();
        return provider.post(enrollData);
    }

    @Override
    public HeaderAndBody onLogin(final String username, final String password) {
        HttpProvider provider = httpProviderFactory.get(loginURL, timeout);
        try {
            provider.get();//Should not be logged in and throw an exception
            throw new IllegalStateException("Login Called on service which was already logged in.");
        } catch (HttpException exception) {
            //If an exception occured that was not a failed login
            if (exception.getStatusCode() != HttpURLConnection.HTTP_FORBIDDEN) {
                throw exception;
            }
            
            Map<String, String> authenticateHeaders = DigestHeaderParser.extractValues(exception.getHeaders().get(WWW_AUTHENTICATE_HEADER));
            realm = authenticateHeaders.get(REALM);
            domain = authenticateHeaders.get(DOMAIN);
            nonce = authenticateHeaders.get(NONCE);
            algorithm = authenticateHeaders.get(ALGORITHM);
            qop = authenticateHeaders.get(QOP_OPTIONS);
            stale = authenticateHeaders.get(STALE);
            opaque = authenticateHeaders.get(OPAQUE);
            
            checkQop(qop);
            
            return provider.get();
        }   
        
        
    }

    @Override
    public void onLogout() {
        HttpProvider provider = httpProviderFactory.get(logoutURL, timeout);
        provider.post("");
    }

    /*
     * Currently only supports auth.
     */
    private void checkQop(String qop) {
        
        
        
        if (qop == null) {
            return;
        } else {
            for (String option : qop.split(",")) {
                if ("auth".equals(option)) {
                    return;
                }
            }
        }
            
        
            throw new IllegalArgumentException(String.format("%s is not a supported qop type.", qop));
        
    }
    
    public String getAuthorizationHeader() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Digest ")
                .append("opaque=\"").append(opaque).append('"');
        
        
        return sb.toString();
    }
    
}
