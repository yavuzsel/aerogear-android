package org.aerogear.android.impl.pipeline;

import java.net.URL;

public final class PipeConfig {

    private final String name;
    private final String endpoint;
    private final Type type;

    public PipeConfig(String name) {
        this(name, name);
    }

    public PipeConfig(String name, String endpoint) {
        this(name, endpoint, Type.REST);
    }

    public PipeConfig(String name, Type type) {
        this(name, name, type);
    }

    public PipeConfig(String name, String endpoint, Type type) {
        this.name = name;
        this.endpoint = endpoint;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Type getType() {
        return type;
    }
}
