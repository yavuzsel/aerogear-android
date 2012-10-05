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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.aerogear.android.core.HttpProvider;

import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.aerogear.android.pipeline.Type.REST;

/**
 * RestAdapter utility class containing useful methods for lifecycle, etc.
 */
public final class RestAdapter<T> implements Pipe<T> {

    private static Gson gson = new Gson();

    private final Class<T[]> exemplar;
    private final HttpProvider httpProvider;

    public RestAdapter(Class<T[]> exemplar, HttpProvider httpProvider) {
        this.exemplar = exemplar;
        this.httpProvider = httpProvider;
    }

    public Type getType() {
        return REST;
    }

    public URL getUrl() {
        return httpProvider.getUrl();
    }

    public T[] read() throws Exception {
        return (T[]) gson.fromJson(new InputStreamReader(httpProvider.get()), exemplar);
    }

    public List<T> getAll(List<T> existingList) throws Exception {
        T[] items = read();
        List<T> ret = existingList;
        if (ret == null) {
            ret = new ArrayList<T>(items.length);
        } else {
            ret.clear();
        }
        Collections.addAll(ret, items);
        return ret;
    }

    public void readWithFilter() {
        // TODO implement
    }

    public void save(T dataObject) throws Exception {
        final Method idGetter = dataObject.getClass().getMethod("getId");

        final Object result = idGetter.invoke(dataObject);
        String id = result == null ? null : result.toString();

        // TODO: Make "id" field configurable
        if (id == null || id.length() == 0) {
            httpProvider.post(gson.toJson(dataObject));
        } else {
            httpProvider.put(id, gson.toJson(dataObject));
        }
    }

    public void remove(String id) throws Exception{
        httpProvider.delete(id);
    }

}
