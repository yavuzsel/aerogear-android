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
package org.aerogear.android.authentication;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aerogear.android.impl.authentication.DefaultAuthenticator;
import org.aerogear.android.impl.authentication.RestAuthenticationModule;
import org.aerogear.android.impl.pipeline.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
/**
 *
 * @author summers
 */
@RunWith(RobolectricTestRunner.class)
public class AuthenticatorTest {
    
    private static final URL SIMPLE_URL;
    private static String SIMPLE_MODULE_NAME  = "simple";
    
    static {
        try {
            SIMPLE_URL = new URL("http", "localhost", 80, "/");
        } catch (MalformedURLException ex) {
            Logger.getLogger(AuthenticatorTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    private static class RestBuilder extends RestAuthenticationModule.Builder {

        public RestBuilder(URL baseURL) {
            super(baseURL);
        }

        
        
        @Override
        public RestAuthenticationModule add(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    @Test
    public void testAddSimpleAuthenticator() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        AuthenticationModule simpleAuthModule = authenticator.add(SIMPLE_MODULE_NAME, new RestBuilder(SIMPLE_URL).build());
        
        assertNotNull(simpleAuthModule);
        
    }
    
    @Test
    public void testAddAndGetSimpleAuthenticator() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        AuthenticationModule simpleAuthModule = authenticator.add(SIMPLE_MODULE_NAME, new RestBuilder(SIMPLE_URL).build());
        assertEquals(simpleAuthModule, authenticator.get(SIMPLE_MODULE_NAME));
    }

    
        @Test
    public void testAddAuthenticator() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        authenticator.auth(AuthType.REST,  SIMPLE_URL).enrollEndpoint("testEnroill").add(SIMPLE_MODULE_NAME);
        AuthenticationModule simpleAuthModule = authenticator.add(SIMPLE_MODULE_NAME, new RestBuilder(SIMPLE_URL).build());
        assertEquals(simpleAuthModule, authenticator.get(SIMPLE_MODULE_NAME));
    }

    
    @Test
    public void testGetNullAuthModule() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        assertNull(authenticator.get(SIMPLE_MODULE_NAME));
    }
}
