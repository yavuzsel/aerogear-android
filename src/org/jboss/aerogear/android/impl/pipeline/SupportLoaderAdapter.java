/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.impl.pipeline;

import java.net.URL;
import java.util.List;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.ReadFilter;
import org.jboss.aerogear.android.impl.pipeline.loader.support.AbstractSupportPipeLoader;
import org.jboss.aerogear.android.impl.pipeline.loader.support.SupportReadLoader;
import org.jboss.aerogear.android.impl.pipeline.loader.support.SupportRemoveLoader;
import org.jboss.aerogear.android.impl.pipeline.loader.support.SupportSaveLoader;
import org.jboss.aerogear.android.pipeline.LoaderPipe;
import org.jboss.aerogear.android.pipeline.Pipe;
import org.jboss.aerogear.android.pipeline.PipeHandler;
import org.jboss.aerogear.android.pipeline.PipeType;
import org.jboss.aerogear.android.pipeline.RequestBuilder;
import org.jboss.aerogear.android.pipeline.ResponseParser;
import org.jboss.aerogear.android.pipeline.support.AbstractFragmentActivityCallback;
import org.jboss.aerogear.android.pipeline.support.AbstractSupportFragmentCallback;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

/**
 * This class wraps a Pipe in an asynchronous Loader.
 *
 * This classes uses Loaders from android.support. If you do not need to support
 * Android devices &lt; version 3.0, consider using {@link LoaderAdapter}
 *
 */
@SuppressWarnings( { "rawtypes", "unchecked" })
public class SupportLoaderAdapter<T> implements LoaderPipe<T>, LoaderManager.LoaderCallbacks<T> {

    private static final String TAG = SupportLoaderAdapter.class.getSimpleName();

    private Multimap<String, Integer> idsForNamedPipes;
    private final Fragment fragment;
    private final FragmentActivity activity;
    private final Handler handler;

    private static enum Methods {

        READ, SAVE, REMOVE
    };

    private final Context applicationContext;
    private final Pipe<T> pipe;
    private final LoaderManager manager;
    private final RequestBuilder<T> requestBuilder;
    private final ResponseParser<T> responseParser;
    /**
     * The name referred to in the idsForNamedPipes
     */
    private final String name;

    @Deprecated
    /**
     * This constructor is reprecated and will be replaced with 
     * SupportLoaderAdapter(FragmentActivity activity, Pipe<T> pipe, String name)
     */
    public SupportLoaderAdapter(FragmentActivity activity, Pipe<T> pipe, Gson gson, String name) {
        this.pipe = pipe;
        this.manager = activity.getSupportLoaderManager();
        this.applicationContext = activity.getApplicationContext();
        this.name = name;
        this.handler = new Handler(Looper.getMainLooper());
        this.activity = activity;
        this.fragment = null;
        this.requestBuilder = new GsonRequestBuilder<T>(gson);
        this.responseParser = new GsonResponseParser<T>(gson);
    }

    @Deprecated
    /**
     * 
     * This constructor is deprecated and will be replaced with 
     * SupportLoaderAdapter(Fragment fragment, Context applicationContext, Pipe<T> pipe, String name)
     */
    public SupportLoaderAdapter(Fragment fragment, Context applicationContext, Pipe<T> pipe, Gson gson, String name) {
        this.pipe = pipe;
        this.manager = fragment.getLoaderManager();
        this.requestBuilder = new GsonRequestBuilder<T>(gson);
        this.applicationContext = applicationContext;
        this.name = name;
        this.handler = new Handler(Looper.getMainLooper());
        this.activity = null;
        this.fragment = fragment;
        this.responseParser = new GsonResponseParser<T>(gson);
    }

    public SupportLoaderAdapter(Fragment fragment, Context applicationContext, Pipe<T> pipe, String name) {
        this.pipe = pipe;
        this.manager = fragment.getLoaderManager();
        this.requestBuilder = pipe.getRequestBuilder();
        this.applicationContext = applicationContext;
        this.name = name;
        this.handler = new Handler(Looper.getMainLooper());
        this.activity = null;
        this.fragment = fragment;
        this.responseParser = pipe.getResponseParser();
    }

    public SupportLoaderAdapter(FragmentActivity activity, Pipe<T> pipe, String name) {
        this.pipe = pipe;
        this.requestBuilder = pipe.getRequestBuilder();
        this.manager = activity.getSupportLoaderManager();
        this.applicationContext = activity.getApplicationContext();
        this.name = name;
        this.handler = new Handler(Looper.getMainLooper());
        this.activity = activity;
        this.fragment = null;
        this.responseParser = pipe.getResponseParser();
    }

    @Override
    public PipeType getType() {
        return pipe.getType();
    }

    @Override
    public URL getUrl() {
        return pipe.getUrl();
    }

    @Override
    public void read(Callback<List<T>> callback) {
        int id = Objects.hashCode(name, callback);

        Bundle bundle = new Bundle();
        bundle.putSerializable(CALLBACK, callback);
        bundle.putSerializable(FILTER, null);
        bundle.putSerializable(METHOD, Methods.READ);
        manager.initLoader(id, bundle, this);
    }

    @Override
    public void readWithFilter(ReadFilter filter, Callback<List<T>> callback) {
        read(filter, callback);
    }

    @Override
    public void read(ReadFilter filter, Callback<List<T>> callback) {
        int id = Objects.hashCode(name, filter, callback);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CALLBACK, callback);
        bundle.putSerializable(FILTER, filter);
        bundle.putSerializable(METHOD, Methods.READ);
        manager.initLoader(id, bundle, this);
    }

    @Override
    public void save(T item, Callback<T> callback) {
        int id = Objects.hashCode(name, item, callback);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CALLBACK, callback);
        bundle.putSerializable(ITEM, requestBuilder.getBody(item));//item may not be serializable, but it has to be able to be handled by the requestBuilder
        bundle.putSerializable(METHOD, Methods.SAVE);
        manager.initLoader(id, bundle, this);
    }

    @Override
    public void remove(String toRemoveId, Callback<Void> callback) {
        int id = Objects.hashCode(name, toRemoveId, callback);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CALLBACK, callback);
        bundle.putSerializable(REMOVE_ID, toRemoveId);
        bundle.putSerializable(METHOD, Methods.REMOVE);
        manager.initLoader(id, bundle, this);
    }

    @Override
    public PipeHandler<T> getHandler() {
        return pipe.getHandler();
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle bundle) {
        this.idsForNamedPipes.put(name, id);
        Methods method = (Methods) bundle.get(METHOD);
        Callback callback = (Callback) bundle.get(CALLBACK);
        Loader loader = null;
        switch (method) {
        case READ: {
            ReadFilter filter = (ReadFilter) bundle.get(FILTER);
            loader = new SupportReadLoader(applicationContext, callback, pipe.getHandler(), filter, this);
        }
            break;
        case REMOVE: {
            String toRemove = Objects.firstNonNull(bundle.getString(REMOVE_ID), "-1");
            loader = new SupportRemoveLoader(applicationContext, callback, pipe.getHandler(), toRemove);
        }
            break;
        case SAVE: {
            byte[] json = bundle.getByteArray(ITEM);
            T item = responseParser.handleResponse(new String(json), pipe.getKlass());
            loader = new SupportSaveLoader(applicationContext, callback, pipe.getHandler(), item);
        }
            break;
        }
        return loader;
    }

    @Override
    public Gson getGson() {
        return requestBuilder instanceof GsonRequestBuilder ? ((GsonRequestBuilder) requestBuilder).getGson() : null;
    }

    @Override
    public RequestBuilder<T> getRequestBuilder() {
        return requestBuilder;
    }

    @Override
    public ResponseParser<T> getResponseParser() {
        return responseParser;
    }

    @Override
    public Class<T> getKlass() {
        return pipe.getKlass();
    }

    @Override
    public void onLoadFinished(Loader<T> loader, final T data) {
        if (!(loader instanceof AbstractSupportPipeLoader)) {
            Log.e(TAG, "Adapter is listening to loaders which it doesn't support");
            throw new IllegalStateException("Adapter is listening to loaders which it doesn't support");
        } else {
            final AbstractSupportPipeLoader<T> supportLoader = (AbstractSupportPipeLoader<T>) loader;
            handler.post(new CallbackHandler<T>(this, supportLoader, data));
        }
    }

    static class CallbackHandler<T> implements Runnable {

        private final SupportLoaderAdapter<T> adapter;
        private final AbstractSupportPipeLoader<T> modernLoader;
        private final T data;

        public CallbackHandler(SupportLoaderAdapter<T> adapter,
                AbstractSupportPipeLoader<T> loader, T data) {
            super();
            this.adapter = adapter;
            this.modernLoader = loader;
            this.data = data;
        }

        @Override
        public void run() {
            if (modernLoader.hasException()) {
                final Exception exception = modernLoader.getException();
                Log.e(TAG, exception.getMessage(), exception);
                if (modernLoader.getCallback() instanceof AbstractSupportFragmentCallback) {
                    adapter.fragmentFailure(modernLoader.getCallback(), exception);
                } else if (modernLoader.getCallback() instanceof AbstractFragmentActivityCallback) {
                    adapter.activityFailure(modernLoader.getCallback(), exception);
                } else {
                    modernLoader.getCallback().onFailure(exception);
                }

            } else {

                if (modernLoader.getCallback() instanceof AbstractSupportFragmentCallback) {
                    adapter.fragmentSuccess(modernLoader.getCallback(), data);
                } else if (modernLoader.getCallback() instanceof AbstractFragmentActivityCallback) {
                    adapter.activitySuccess(modernLoader.getCallback(), data);
                } else {
                    modernLoader.getCallback().onSuccess(data);
                }
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        //Gotta do something, though I don't know what
    }

    @Override
    public void reset() {
        for (Integer id : this.idsForNamedPipes.get(name)) {
            Loader loader = manager.getLoader(id);
            if (loader != null) {
                manager.destroyLoader(id);
            }
        }
        idsForNamedPipes.removeAll(name);
    }

    @Override
    public void cancel() {
        for (Integer id : this.idsForNamedPipes.get(name)) {
            Loader loader = manager.getLoader(id);
            if (loader != null) {
                // TODO: cancel loader
            }
        }
    }

    @Override
    public void setLoaderIds(Multimap<String, Integer> idsForNamedPipes) {
        this.idsForNamedPipes = idsForNamedPipes;
    }

    private void fragmentSuccess(Callback<T> typelessCallback, T data) {
        AbstractSupportFragmentCallback callback = (AbstractSupportFragmentCallback) typelessCallback;
        callback.setFragment(fragment);
        callback.onSuccess(data);
        callback.setFragment(null);
    }

    private void fragmentFailure(Callback<T> typelessCallback, Exception exception) {
        AbstractSupportFragmentCallback callback = (AbstractSupportFragmentCallback) typelessCallback;
        callback.setFragment(fragment);
        callback.onFailure(exception);
        callback.setFragment(null);
    }

    private void activitySuccess(Callback<T> typelessCallback, T data) {
        AbstractFragmentActivityCallback callback = (AbstractFragmentActivityCallback) typelessCallback;
        callback.setFragmentActivity(activity);
        callback.onSuccess(data);
        callback.setFragmentActivity(null);
    }

    private void activityFailure(Callback<T> typelessCallback, Exception exception) {
        AbstractFragmentActivityCallback callback = (AbstractFragmentActivityCallback) typelessCallback;
        callback.setFragmentActivity(activity);
        callback.onFailure(exception);
        callback.setFragmentActivity(null);
    }
}
