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

import com.google.gson.Gson;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.StatusLineStub;
import com.xtremelabs.robolectric.tester.org.apache.http.RequestMatcher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aerogear.android.Builder;
import org.aerogear.android.Callback;
import org.aerogear.android.authentication.impl.RestAuthenticationModule;
import org.aerogear.android.core.HeaderAndBodyMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author summers
 */
@RunWith(RobolectricTestRunner.class)
public class RestAuthenticationModuleTest {
    private static final String TOKEN = "a016b29b-da74-4833-aa50-43c55788c528";
    private static final Builder<RestAuthenticationModule> BUILDER;
    private static final RequestMatcher JOHN_LOGIN_MATCHER = new RequestMatcher() {
        
        @Override
        public boolean matches(HttpRequest request) {
            if (request instanceof HttpEntityEnclosingRequest) {
                try {
                HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
                JSONObject data = new JSONObject(EntityUtils.toString(entity));
                return (data.getString("username").equalsIgnoreCase("john") &&
                        data.getString("password").equalsIgnoreCase("password") 
                        );
                } catch (Throwable t ) {
                    return false;
                }
            }
            return false;
        }
    };

    private static final HttpResponse VALID_LOGIN = new BasicHttpResponse(new StatusLineStub()) {

        @Override
        public Header getFirstHeader(String name) {
            if (name.equals(RestAuthenticationModule.TOKEN_HEADER)) {
                return new BasicHeader(RestAuthenticationModule.TOKEN_HEADER, 
                          TOKEN);//Magic Number
                
            }
            return super.getFirstHeader(name);
        }

        @Override
        public Header[] getAllHeaders() {
            return new Header[] {
                new BasicHeader(RestAuthenticationModule.TOKEN_HEADER, TOKEN)
            };
        }

        
        
        
        @Override
        public HttpEntity getEntity() {
            return new BasicHttpEntity() {

                @Override
                public InputStream getContent() throws IllegalStateException {
                    return new ByteArrayInputStream(("{\"username\":\"john\","
                                                   + "\"roles\":[\"admin\"],"
                                                   + "\"logged\":\"true\"}").getBytes()
                            );
                }
                
            };
        }
        
        
        
        @Override
        public StatusLine getStatusLine() {
            return new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "");
        }
        
    };
    
    static {
        try {
            BUILDER = new RestAuthenticationModule.Builder().baseURL(new URL("http://localhost:8080/todo-server"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestAuthenticationModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    private final static class SimpleCallback implements Callback<HeaderAndBodyMap> {

        HeaderAndBodyMap data;
        Exception exception;
        
        @Override
        public void onSuccess(HeaderAndBodyMap data) {
            this.data = data;
        }

        @Override
        public void onFailure(Exception e) {
            this.exception = e;
        }
    
    }
    
    @Before
    public void setup() {
        Robolectric.setDefaultHttpResponse(401, "Unauthorized");
    }
    
    @After
    public void cleaRules() {
        Robolectric.clearHttpResponseRules();  
        Robolectric.clearPendingHttpResponses();
    }
    
    @Test(timeout=5000L)
    public void loginFails() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        SimpleCallback callback = new SimpleCallback();
        module.login("john", "password", callback);
        
        while(!Robolectric.httpRequestWasMade()) {
        
        };
        Assert.assertNotNull(callback.exception);
        Assert.assertFalse(module.isAuthenticated());
    }
    
    
    @Test(timeout=50000L)
    public void loginSucceeds() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        Robolectric.addHttpResponseRule(JOHN_LOGIN_MATCHER, VALID_LOGIN);
        SimpleCallback callback = new SimpleCallback();
        module.login("john", "password", callback);
        
        while(!Robolectric.httpRequestWasMade()) {
        
        };
        Assert.assertNull(callback.exception);
        Assert.assertNotNull(callback.data);
        Assert.assertTrue(module.isAuthenticated());
        Assert.assertEquals(TOKEN, module.getAuthToken());
    }
}
