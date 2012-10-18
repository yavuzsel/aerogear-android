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

package org.aerogear.android.pipeline;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Pipeline {

    private final URL baseURL;

    private Map<String, Pipe> pipes = new HashMap<String, Pipe>();

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

    /**
     * An initializer method to instantiate the Pipeline, which
     * contains a RESTful name.
     *
     * @param name the endpoint name of the first Pipe object
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     *
     */
    public Pipeline(String name, Class klass, URL baseURL) {
        this(name, klass, baseURL, name, Type.REST);
    }

    /**
     * An initializer method to instantiate the Pipeline, which
     * contains a name object. The actual type is determined by the type argument.
     *
     * @param name the endpoint name of the first Pipe object
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     * @param type the type of the actual pipe/connection
     *
     */
    public Pipeline(String name, Class klass, URL baseURL, Type type) {
        this(name, klass, baseURL, name, type);
    }

    /**
     * An initializer method to instantiate the Pipeline, which
     * contains a RESTful name. The RESTful endpoint is determined by the endpoint argument.
     *
     * @param name the logical name of the first Pipe object
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     * @param endpoint the serivce endpoint name
     *
     */
    public Pipeline(String name, Class klass, URL baseURL, String endpoint) {
        this(name, klass, baseURL, endpoint, Type.REST);
    }

    /**
     * An initializer method to instantiate the Pipeline, which
     * contains a RESTful name. The RESTful endpoint is determined by the endpoint argument.
     *
     * @param name the logical name of the first Pipe object
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     * @param endpoint the serivce endpoint name
     * @param type the type of the actual pipe/connection
     *
     */
    public Pipeline(String name, Class klass, URL baseURL, String endpoint, Type type) {
        this.baseURL = baseURL;
        this.add(name, klass, baseURL, endpoint, type);
    }

    /**
     * Adds a new RESTful pipe to the Pipeline object,
     * leveraging the given baseURL argument.
     *
     * @param name the endpoint name of the actual pipe
     * @param klass class that represents the model of the pipe
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass) {
        return this.add(name, klass, baseURL, name, Type.REST);
    }

    /**
     * Adds a new RESTful pipe to the Pipeline object,
     * leveraging the given baseURL argument.
     *
     * @param name     the name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param endpoint the serivce endpoint, if differs from the pipe name.
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, String endpoint) {
        return this.add(name, klass, baseURL, endpoint, Type.REST);
    }

    /**
     * Adds a new pipe (server connection) to the Pipeline object,
     * leveraging the given baseURL argument.
     *
     * @param name the endpoint name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param type the type of the actual pipe/connection
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, Type type) {
        return this.add(name, klass, baseURL, name, type);
    }

    /**
     * Adds a new pipe (server connection) to the Pipeline object,
     * leveraging the given baseURL argument.
     *
     * @param name the logical name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param endpoint the serivce endpoint, if differs from the pipe name.
     * @param type the type of the actual pipe/connection
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, String endpoint, Type type) {
        return this.add(name, klass, baseURL, endpoint, type);
    }

    /**
     * Adds a new RESTful pipe to the Pipeline object
     *
     * @param name the endpoint name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, URL baseURL) {
        return this.add(name, klass, baseURL, name, Type.REST);
    }

    /**
     * Adds a new RESTful pipe to the Pipeline object
     *
     * @param name the name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     * @param endpoint the serivce endpoint, if differs from the pipe name.
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, URL baseURL, String endpoint) {
        return this.add(name, klass, baseURL, endpoint, Type.REST);
    }

    /**
     * Adds a new pipe (server connection) to the Pipeline object
     *
     * @param name the endpoint name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     * @param type the type of the actual pipe/connection
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, URL baseURL, Type type) {
        return this.add(name, klass, baseURL, name, type);
    }

    /**
     * Adds a new pipe (server connection) to the Pipeline object
     *
     * @param name the logical name of the actual pipe
     * @param klass class that represents the model of the pipe
     * @param baseURL the URL of the server
     * @param endpoint the serivce endpoint, if differs from the pipe name.
     * @param type the type of the actual pipe/connection
     *
     * @return the new created Pipe object
     */
    public Pipe add(String name, Class klass, URL baseURL, String endpoint, Type type) {
        return this.addPipe(name, klass, appendEndpoint(baseURL, endpoint), type);
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

    private Pipe addPipe(String name, Class klass, URL url, Type type) {
        Pipe pipe = AdapterFactory.createPipe(type, klass, url);
        pipes.put(name, pipe);
        return pipe;
    }

    private URL appendEndpoint(URL baseURL, String endpoint) {

        try {
            // TODO Move to helper?
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
