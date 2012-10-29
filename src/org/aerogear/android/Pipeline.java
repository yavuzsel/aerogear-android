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
import org.aerogear.android.impl.pipeline.AdapterFactory;
import org.aerogear.android.impl.pipeline.Type;
import org.aerogear.android.pipeline.Pipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.aerogear.android.impl.pipeline.Type.REST;

/**
 * A {@link Pipeline} represents a ‘collection’ of server connections (aka {@link Pipe}s).
 + The {@link Pipeline} contains some simple management APIs to create or remove {@link Pipe}s objects.
 */
public class Pipeline {

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

    public PipeBuilderMandatoryName pipe() {
        return new BuilderImpl(baseURL);
    }

    private final class BuilderImpl implements PipeBuilderMandatoryName,
            PipeBuilderMandatoryClass, PipeBuilder {

        private String name;
        private Class klass;
        private String endpoint;
        private Type type = REST;
        private URL url;

        public BuilderImpl(URL url) {
            this.url = url;
        }

        @Override
        public PipeBuilderMandatoryClass name(String name) {
            this.name = name;
            this.endpoint = name;
            return this;
        }

        @Override
        public PipeBuilder useClass(Class klass) {
            this.klass = klass;
            return this;
        }


        @Override
        public PipeBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        @Override
        public PipeBuilder type(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public PipeBuilder url(URL url) {
            this.url = url;
            return this;
        }

        @Override
        public Pipe buildAndAdd() {
            Pipe pipe = AdapterFactory.createPipe(type, klass, appendEndpoint(url, endpoint));
            pipes.put(name, pipe);
            return pipe;
        }

        private URL appendEndpoint(URL baseURL, String endpoint) {

            try {
                if( !baseURL.toString().endsWith("/")) {
                    endpoint = "/" + endpoint;
                }
                return new URL(baseURL + endpoint + "/");
            } catch (MalformedURLException e) {
                Log.e("AeroGear", e.getMessage());
                return null;
            }
        }

    }

    public static interface PipeBuilderMandatoryName {
        public PipeBuilderMandatoryClass name(String name);
    }

    public static interface PipeBuilderMandatoryClass {
        public PipeBuilder useClass(Class klass);
    }

    public static interface PipeBuilder {
        public PipeBuilder endpoint(String endpoint);
        public PipeBuilder type(Type type);
        public PipeBuilder url(URL url);
        public Pipe buildAndAdd();
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
