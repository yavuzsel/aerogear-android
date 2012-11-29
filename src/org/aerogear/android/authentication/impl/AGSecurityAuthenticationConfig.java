package org.aerogear.android.authentication.impl;

import org.aerogear.android.authentication.AuthenticationConfig;

/**
 * A config object for AG Security services.
 * @see <a href="https://github.com/aerogear/aerogear-security#endpoints-definition">AG Security Endpoint Doc</a>
 */
public final class AGSecurityAuthenticationConfig extends AuthenticationConfig {

    private String tokenHeaderName = "Auth-Token";

    public String getTokenHeaderName() {
        return tokenHeaderName;
    }

    public void setTokenHeaderName(String tokenHeaderName) {
        this.tokenHeaderName = tokenHeaderName;
    }

}
