package org.aerogear.android.impl.pipeline;

import com.google.gson.GsonBuilder;
import org.aerogear.android.authentication.AuthenticationModule;
import org.aerogear.android.core.TypeDescriptor;
import org.aerogear.android.pipeline.PipeType;

import java.net.URL;

public final class PipeConfig {

    private URL baseURL;
    private String name;
    private String endpoint;
    private PipeType type = PipeTypes.REST;
    private GsonBuilder gsonBuilder;
    private AuthenticationModule authModule;

    public PipeConfig(URL baseURL, Class klass) {
        this.baseURL = baseURL;
        this.name = klass.getSimpleName().toLowerCase();
        this.endpoint = name;
        this.type = PipeTypes.REST;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(URL baseURL) {
        this.baseURL = baseURL;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public PipeType getType() {
        return type;
    }

    public void setType(PipeType type) {
        this.type = type;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public void setGsonBuilder(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }
    
    public AuthenticationModule getAuthModule(){
        return authModule;
    }
    
    public void setAuthModule(AuthenticationModule authModule) {
        this.authModule = authModule;
    }
}
