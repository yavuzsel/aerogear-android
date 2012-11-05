package org.aerogear.android.impl.pipeline;

import org.aerogear.android.pipeline.PipeType;

import java.net.URL;

public final class PipeConfig {

    private String name;
    private URL url;
    private String endpoint;
    private PipeType type;

    public PipeConfig() {
    }

    public PipeConfig(URL baseURL) {
        this.url = baseURL;
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
