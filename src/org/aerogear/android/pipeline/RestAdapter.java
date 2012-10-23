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

import android.os.AsyncTask;
import com.google.gson.Gson;
import org.aerogear.android.Callback;
import org.aerogear.android.core.HttpProvider;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.aerogear.android.pipeline.Type.REST;

/**
 * RestAdapter utility class containing useful methods for lifecycle, etc.
 */
final class RestAdapter<T> implements Pipe<T> {

    private final static Gson gson = new Gson();

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

    public void read(final Callback<T[]> callback) {
        new AsyncTask<Void, Void, AsyncTaskResult<T[]>>() {
            @Override
            protected AsyncTaskResult doInBackground(Void... voids) {
                try {
                    return new AsyncTaskResult(gson.fromJson(new String(httpProvider.get()), exemplar));
                } catch (Exception e) {
                    return new AsyncTaskResult(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<T[]> asyncTaskResult) {
                if ( asyncTaskResult.getError() != null ) {
                    callback.onFailure(asyncTaskResult.getError());
                } else {
                    callback.onSuccess(asyncTaskResult.getResult());
                }
            }
        }.execute();
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
                callback.onSuccess(ret);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        };

        try {
            read(rawCallback);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public void readWithFilter() {
        // TODO implement
    }

    public void save(final T data, final Callback<T> callback) {

        final String id;

        // TODO: Make "id" field configurable
        try {
            Method idGetter = data.getClass().getMethod("getId");
            Object result = idGetter.invoke(data);
            id = result == null ? null : result.toString();
        } catch (Exception e) {
            callback.onFailure(e);
            return;
        }

        new AsyncTask<Void, Void, AsyncTaskResult<T>>() {
            @Override
            protected AsyncTaskResult doInBackground(Void... voids) {
                try {
                    if (id == null || id.length() == 0) {
                        httpProvider.post(gson.toJson(data));
                    } else {
                        httpProvider.put(id, gson.toJson(data));
                    }
                    return new AsyncTaskResult(null);
                } catch (Exception e) {
                    return new AsyncTaskResult(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<T> asyncTaskResult) {
                if ( asyncTaskResult.getError() != null ) {
                    callback.onFailure(asyncTaskResult.getError());
                } else {
                    callback.onSuccess(asyncTaskResult.getResult());
                }
            }
        }.execute();

    }

    public void remove(final String id, final Callback<Void> callback) {
        new AsyncTask<Void, Void, AsyncTaskResult<byte[]>>() {
            @Override
            protected AsyncTaskResult doInBackground(Void... voids) {
                try {
                    return new AsyncTaskResult(httpProvider.delete(id));
                } catch (Exception e) {
                    return new AsyncTaskResult(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<byte[]> asyncTaskResult) {
                if ( asyncTaskResult.getError() != null ) {
                    callback.onFailure(asyncTaskResult.getError());
                } else {
                    callback.onSuccess(null);
                }
            }
        }.execute();
    }

    private class AsyncTaskResult<T> {

        private T result;
        private Exception error;

        public AsyncTaskResult(T result) {
            this.result = result;
        }

        public AsyncTaskResult(Exception error) {
            this.error = error;
        }

        public T getResult() {
            return result;
        }

        public Exception getError() {
            return error;
        }

    }

}
