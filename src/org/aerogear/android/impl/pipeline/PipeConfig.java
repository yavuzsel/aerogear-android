package org.aerogear.android.impl.pipeline;

import org.aerogear.android.pipeline.PipeType;

import java.net.URL;

public final class PipeConfig {

    private final String name;
    private final URL url;
    private final String endpoint;
    private final PipeType type;

    public PipeConfig(String name, URL url) {
        this(name, url, name);
    }

    public PipeConfig(String name, URL url, String endpoint) {
        this(name, url, endpoint, Types.REST);
    }

    public PipeConfig(String name, URL url, PipeType type) {
        this(name, url, name, type);
    }

    public PipeConfig(String name, URL url, String endpoint, PipeType type) {
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

    public PipeType getType() {
        return type;
    }
}
