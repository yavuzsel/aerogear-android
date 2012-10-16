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

package org.aerogear.android.core;

import android.util.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public final class HttpRestProvider implements HttpProvider {

    private static final String TAG = "AeroGear";

    private final URL url;
    private static HttpClient client;
    static {
        HttpParams params = new BasicHttpParams();
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final ThreadSafeClientConnManager connManager =
                new ThreadSafeClientConnManager(params, registry);
        client = new DefaultHttpClient(connManager, params);
    }

    public HttpRestProvider(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public byte [] get() throws RuntimeException {
        try {
            return execute(new HttpGet(url.toString()));
        } catch (IOException e) {
            Log.e(TAG, "Error on GET of " + url, e);
            throw new RuntimeException(e);
        }
    }

    public byte [] post(String data) throws RuntimeException {
        HttpPost post = new HttpPost(url.toString());
        addBodyRequest(post, data);
        try {
            return execute(post);
        } catch (IOException e) {
            Log.e(TAG, "Error on POST of " + url, e);
            throw new RuntimeException(e);
        }
    }

    public byte [] put(String id, String data) throws RuntimeException {
        HttpPut put = new HttpPut(appendIdToURL(id));
        addBodyRequest(put, data);
        try {
            return execute(put);
        } catch (IOException e) {
            Log.e(TAG, "Error on PUT of " + url, e);
            throw new RuntimeException(e);
        }
    }

    public byte [] delete(String id) throws RuntimeException {
        HttpDelete delete = new HttpDelete(appendIdToURL(id));
        try {
            return execute(delete);
        } catch (IOException e) {
            Log.e(TAG, "Error on DELETE of " + url, e);
            throw new RuntimeException(e);
        }
    }

    private void addBodyRequest(HttpEntityEnclosingRequestBase requestBase, String data) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        requestBase.setEntity(entity);
    }

    private byte [] execute(HttpRequestBase method) throws IOException {
        method.setHeader("Accept", "application/json");
        method.setHeader("Content-type", "application/json");
        return EntityUtils.toByteArray(client.execute(method).getEntity());
    }

    private String appendIdToURL(String id) {
        StringBuilder newUrl = new StringBuilder(url.toString());
        if( !url.toString().endsWith("/")) {
            newUrl.append("/");
        }
        newUrl.append(id);
        return newUrl.toString();
    }

}
