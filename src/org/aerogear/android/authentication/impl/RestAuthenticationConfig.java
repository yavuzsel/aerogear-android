
package org.aerogear.android.authentication.impl;

import org.aerogear.android.authentication.AuthenticationConfig;


public final class RestAuthenticationConfig extends AuthenticationConfig {
    
    private String tokenHeaderName = "Auth-Token";

    public String getTokenHeaderName() {
        return tokenHeaderName;
    }

    public void setTokenHeaderName(String tokenHeaderName) {
        this.tokenHeaderName = tokenHeaderName;
    }

}
