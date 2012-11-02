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

package org.aerogear.android;

import android.util.Log;
import org.aerogear.android.AdapterFactory;
import org.aerogear.android.impl.pipeline.PipeConfig;
import org.aerogear.android.impl.pipeline.Type;
import org.aerogear.android.pipeline.Pipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.aerogear.android.impl.pipeline.Type.REST;

/**
 * A {@link Pipeline} represents a ‘collection’ of server connections (aka {@link Pipe}s).
 * The {@link Pipeline} contains some simple management APIs to create or remove {@link Pipe}s objects.
 */
public final class Pipeline {

    private final URL baseURL;

    private final Map<String, Pipe> pipes = new HashMap<String, Pipe>();

    /**
     * An initializer method to instantiate the Pipeline,
     *
     * @param baseURL the URL of the server
     *
     */
    public Pipeline(URL baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * An initializer method to instantiate the Pipeline,
     *
     * @param baseURL the URL of the server
     *
     */
    public Pipeline(String baseURL) {
        try {
            this.baseURL = new URL(baseURL);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Pipe pipe(Class klass) {
        PipeConfig config = new PipeConfig(klass.getSimpleName().toLowerCase());
        return pipe(klass, config);
    }

    public Pipe pipe(Class klass, PipeConfig config) {
        Pipe pipe = AdapterFactory.createPipe(klass, baseURL, config);
        pipes.put(config.getName(), pipe);
        return pipe;
    }

    /**
     * Removes a pipe from the Pipeline object
     *
     * @param name the name of the actual pipe
     *
     * @return the new created Pipe object
     */
    public Pipe remove(String name) {
        return pipes.remove(name);
    }

    /**
     * Look up for a pipe object.
     *
     * @param name the name of the actual pipe
     *
     * @return the new created Pipe object
     */
    public Pipe get(String name) {
        return pipes.get(name);
    }

}
