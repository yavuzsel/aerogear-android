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

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * AeroGear utility class containing useful methods for lifecycle, etc.
 *
 * TODO: This is just a sketch - needs fleshing out and should live in separate project
 * TODO: Layer this better (raw data => object mapping => threading/async/callbacks)
 */
public final class AeroGear {
    static String apiKey;
    static String rootUrl;
    static Gson gson = new Gson();
    static Utils utils = new Utils();

    public static void setUtils(Utils utils) {
        AeroGear.utils = utils;
    }

    private AeroGear() {
    }

    public static void initialize(String apiKey, String rootUrl) {
        AeroGear.apiKey = apiKey;
        AeroGear.rootUrl = rootUrl;
    }

    /**
     * Get a deserialized JSON response from the back end (using gson).
     *
     * @param url the relative (from the rootUrl) path to the data we're retrieving
     * @param dataClass the Java Class we're expecting from the backend
     * @return a deserialized Java object with data filled in from the backend response
     * @throws Exception if something goes awry
     */
    public static <T> T get(String url, Class<T> dataClass) throws Exception {
        InputStream is = utils.get(url);

        return deserialize(dataClass, is);
    }

    private static <T> T deserialize(Class<T> dataClass, InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        return gson.fromJson(reader, dataClass);
    }

    public static <T> void save(String url, T dataObject) throws Exception {
        final Method idGetter = dataObject.getClass().getMethod("getId");

        final Object result = idGetter.invoke(dataObject);
        String id = result == null ? null : result.toString();

        // TODO: Make "id" field configurable
        if (id == null || id.length() == 0) {
            utils.post(url, gson.toJson(dataObject));
        } else {
            utils.put(url + "/" + id, gson.toJson(dataObject));
        }
    }

    public static void delete(String url, String id) {
        utils.delete(url + "/" + id);
    }
}
