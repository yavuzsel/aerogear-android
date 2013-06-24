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

import java.net.URL;
import org.jboss.aerogear.android.authentication.AbstractAuthenticationModule;
import org.jboss.aerogear.android.authentication.AuthorizationFields;

/**
 * This class provides Authentication using HTTP Digest
 *
 * As per the <a href="http://www.ietf.org/rfc/rfc2617.txt">HTTP RFC</a> this
 * class will cache credentials and consumed by {@link Pipe} requests. This
 * module assumes that credentials provided are valid and will never fail on {@link #login(java.lang.String, java.lang.String, org.jboss.aerogear.android.Callback)
 * }
 * or {@link AGSecurityAuthenticationModule#logout(org.jboss.aerogear.android.Callback)
 * }.
 *
 * {@link #enroll(java.util.Map, org.jboss.aerogear.android.Callback) } is not
 * supported and will always fail.
 *
 */
public class HttpDigestAuthenticationModule extends AbstractAuthenticationModule {

    @Override
    public URL getBaseURL() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLoginEndpoint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLogoutEndpoint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEnrollEndpoint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLoggedIn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AuthorizationFields getAuthorizationFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
