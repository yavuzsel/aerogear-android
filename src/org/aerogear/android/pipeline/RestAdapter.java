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

import android.os.Handler;
import com.google.gson.Gson;
import org.aerogear.android.Callback;
import org.aerogear.android.core.HttpProvider;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.aerogear.android.pipeline.Type.REST;

/**
 * RestAdapter utility class containing useful methods for lifecycle, etc.
 */
public final class RestAdapter<T> implements Pipe<T> {

    private static Gson gson = new Gson();
    private static ThreadPoolExecutor threadPool;

    private final Class<T[]> exemplar;
    private final HttpProvider httpProvider;
    private Handler handler;

    public RestAdapter(Class<T[]> exemplar, HttpProvider httpProvider) {
        this.exemplar = exemplar;
        this.httpProvider = httpProvider;

        handler = new Handler(); // TODO: Confirm this is on a Looper-enabled thread
        threadPool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    }

    public Type getType() {
        return REST;
    }

    public URL getUrl() {
        return httpProvider.getUrl();
    }

    public void read(final Callback<T[]> callback) {
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                final T[] result;
                try {
                    result = gson.fromJson(new String(httpProvider.get()), exemplar);
                } catch (Exception e) {
                    postFailure(callback, e);
                    return;
                }
                postSuccess(callback, result);
            }
        };
        threadPool.submit(runner);
    }

    public void getAll(final List<T> existingList, final Callback<List<T>> callback) {
        Callback<T[]> rawCallback = new Callback<T[]>() {
            @Override
            public void onSuccess(T[] data) {
                List<T> ret = existingList;
                if (ret == null) {
                    ret = new ArrayList<T>(data.length);
                } else {
                    ret.clear();
                }
                Collections.addAll(ret, data);
                postSuccess(callback, ret);
            }

            @Override
            public void onFailure(Exception e) {
                postFailure(callback, e);
            }
        };

        try {
            read(rawCallback);
        } catch (Exception e) {
            postFailure(callback, e);
        }
    }

    public void readWithFilter() {
        // TODO implement
    }

    public void save(final T dataObject, final Callback<T> callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Method idGetter = null;
                try {
                    idGetter = dataObject.getClass().getMethod("getId");
                } catch (NoSuchMethodException e) {
                    postFailure(callback, e);
                }

                Object result = null;
                try {
                    result = idGetter.invoke(dataObject);
                } catch (Exception e) {
                    postFailure(callback, e);
                }
                String id = result == null ? null : result.toString();

                try {
                    // TODO: Make "id" field configurable
                    if (id == null || id.length() == 0) {
                        httpProvider.post(gson.toJson(dataObject));
                    } else {
                        httpProvider.put(id, gson.toJson(dataObject));
                    }
                    postSuccess(callback, null);
                } catch (RuntimeException e) {
                    postFailure(callback, e);
                }
            }
        };
        threadPool.submit(runnable);
    }

    public void remove(final String id, final Callback<Void> callback) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    httpProvider.delete(id);
                    callback.onSuccess(null);
                } catch (RuntimeException e) {
                    callback.onFailure(e);
                }
            }
        });
    }

    /**
     * Indicate success by invoking the Callback's onSuccess (on the main thread)
     *
     * @param callback the user's Callback
     * @param data the result data
     */
    private <J> void postSuccess(final Callback<J> callback, final J data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(data);
            }
        });
    }

    /**
     * Indicate failure by invoking the Callback's onFailure (on the main thread)
     *
     * @param callback the user's Callback
     * @param e an Exception to be passed to the user
     */
    private <J> void postFailure(final Callback<J> callback, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(e);
            }
        });
    }
}
