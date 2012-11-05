package org.aerogear.android.impl.pipeline;

import org.aerogear.android.pipeline.PipeType;

import java.net.URL;

public final class PipeConfig {

    private URL url;
    private String name;
    private String endpoint;
    private PipeType type;

    public PipeConfig(Class klass, URL baseURL) {
        this.url = baseURL;
        this.name = klass.getSimpleName().toLowerCase();
        this.endpoint = name;
        this.type = Types.REST;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
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
}
