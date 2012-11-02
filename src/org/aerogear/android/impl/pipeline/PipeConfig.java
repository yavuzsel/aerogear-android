package org.aerogear.android.impl.pipeline;

import java.net.URL;

public final class PipeConfig {

    private final String name;
    private final URL url;
    private final String endpoint;
    private final Type type;

    public PipeConfig(String name, URL url) {
        this(name, url, name);
    }

    public PipeConfig(String name, URL url, String endpoint) {
        this(name, url, endpoint, Type.REST);
    }

    public PipeConfig(String name, URL url, Type type) {
        this(name, url, name, type);
    }

    public PipeConfig(String name, URL url, String endpoint, Type type) {
        this.name = name;
        this.url = url;
        this.endpoint = endpoint;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Type getType() {
        return type;
    }
}
