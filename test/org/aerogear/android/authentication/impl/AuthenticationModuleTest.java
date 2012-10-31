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

import com.xtremelabs.robolectric.shadows.StatusLineStub;
import com.xtremelabs.robolectric.tester.org.apache.http.RequestMatcher;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.aerogear.android.Callback;
import org.aerogear.android.authentication.impl.RestAuthenticationModule;
import org.aerogear.android.core.HeaderAndBody;
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

/**
 * This interface sets up all of the static values for 
 * {@link RestAuthenticationModuleTest}
 */
public interface AuthenticationModuleTest {

    /**
     * Default AUTH Token
     */
    static final String TOKEN = "a016b29b-da74-4833-aa50-43c55788c528";
    
    static final String PASSING_USERNAME = "spittman";
    static final String FAILING_USERNAME = "fail";
    static final String LOGIN_PASSWORD = "password";
    static final String ENROLL_PASSWORD = "spittman";
    
    static final RequestMatcher LOGIN_MATCHER = new RequestMatcher() {
        @Override
        public boolean matches(HttpRequest request) {
            if (request instanceof HttpEntityEnclosingRequest) {
                try {
                    HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                    JSONObject data = new JSONObject(EntityUtils.toString(entity));
                    return (data.getString("username").equalsIgnoreCase(PASSING_USERNAME)
                            && data.getString("password").equalsIgnoreCase(LOGIN_PASSWORD));
                } catch (Throwable t) {
                    return false;
                }
            }
            return false;
        }
    };
    static final RequestMatcher ENROLL_PASS_MATCHER = new RequestMatcher() {
        @Override
        public boolean matches(HttpRequest request) {
            if (request instanceof HttpEntityEnclosingRequest) {
                try {
                    HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                    JSONObject data = new JSONObject(EntityUtils.toString(entity));
                    return (data.getString("username").equalsIgnoreCase(PASSING_USERNAME)
                            && data.getString("password").equalsIgnoreCase(ENROLL_PASSWORD));
                } catch (Throwable t) {
                    return false;
                }
            }
            return false;
        }
    };
    
    static final RequestMatcher ENROLL_FAIL_MATCHER = new RequestMatcher() {
        @Override
        public boolean matches(HttpRequest request) {
            if (request instanceof HttpEntityEnclosingRequest) {
                try {
                    HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                    JSONObject data = new JSONObject(EntityUtils.toString(entity));
                    return (data.getString("username").equalsIgnoreCase(FAILING_USERNAME)
                            && data.getString("password").equalsIgnoreCase(ENROLL_PASSWORD));
                } catch (Throwable t) {
                    return false;
                }
            }
            return false;
        }
    };
    
    static final HttpResponse VALID_LOGIN = new BasicHttpResponse(new StatusLineStub()) {
        private String  TOKEN_HEADER = "Auth-Token";
        @Override
        public Header getFirstHeader(String name) {
            if (name.equals(TOKEN_HEADER)) {
                return new BasicHeader(TOKEN_HEADER,
                        TOKEN);//Magic Number

            }
            return super.getFirstHeader(name);
        }

        @Override
        public Header[] getAllHeaders() {
            return new Header[]{
                        new BasicHeader(TOKEN_HEADER, TOKEN)
                    };
        }

        @Override
        public HttpEntity getEntity() {
            return new BasicHttpEntity() {
                @Override
                public InputStream getContent() throws IllegalStateException {
                    return new ByteArrayInputStream(("{\"username\":\"" + PASSING_USERNAME + "\","
                            + "\"roles\":[\"admin\"],"
                            + "\"logged\":\"true\"}").getBytes());
                }
            };
        }

        @Override
        public StatusLine getStatusLine() {
            return new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "");
        }
    };

    static final HttpResponse ENROLL_PASS = VALID_LOGIN;
    
    static final HttpResponse ENROLL_FAIL = new BasicHttpResponse(new StatusLineStub()) {
        @Override
        public Header getFirstHeader(String name) {
                return new BasicHeader("","");
        }

        @Override
        public Header[] getAllHeaders() {
            return new Header[]{};
        }

        @Override
        public HttpEntity getEntity() {
            return new BasicHttpEntity() {
                @Override
                public InputStream getContent() throws IllegalStateException {
                    return new ByteArrayInputStream(("").getBytes());
                }
            };
        }

        @Override
        public StatusLine getStatusLine() {
            return new BasicStatusLine(new ProtocolVersion("http", 1, 1), 400, "");
        }
    };
    
    final class SimpleCallback implements Callback<HeaderAndBody> {

        HeaderAndBody data;
        Exception exception;

        @Override
        public void onSuccess(HeaderAndBody data) {
            this.data = data;
        }

        @Override
        public void onFailure(Exception e) {
            this.exception = e;
        }
    }
    
    final class VoidCallback implements Callback<Void> {

        
        Exception exception;

        @Override
        public void onSuccess(Void data) {
            
        }

        @Override
        public void onFailure(Exception e) {
            this.exception = e;
        }
    }
}
