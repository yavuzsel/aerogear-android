/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.authentication.impl;

import com.google.gson.JsonObject;
import java.net.URL;
import java.util.Map;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpProvider;
import org.json.JSONObject;

class AGSecurityAuthenticationModuleRunner extends AbstractAuthenticationModuleRunner {

    
    /**
     * @param baseURL
     * @param config
     * @throws IllegalArgumentException if an endpoint can not be appended to
     * baseURL
     */
    public AGSecurityAuthenticationModuleRunner(URL baseURL, AuthenticationConfig config) {
        super(baseURL, config);
    }
    
    String buildLoginData(String username, String password) {
        JsonObject response = new JsonObject();
        response.addProperty("username", username);
        response.addProperty("password", password);
        return response.toString();
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
        String loginData = buildLoginData(username, password);
        return provider.post(loginData);
    }

    @Override
    public void onLogout() {
        HttpProvider provider = httpProviderFactory.get(logoutURL, timeout);
        provider.post("");
    }
}
