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
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class HttpRestProvider implements HttpProvider {

    private static final String TAG = "AeroGear";

    private final URL url;
    private static final HttpClient client = new DefaultHttpClient();

    public HttpRestProvider(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public InputStream get() throws RuntimeException {
        try {
            return execute(new HttpGet(url.toString()));
        } catch (IOException e) {
            Log.e(TAG, "Error on GET of " + url, e);
            throw new RuntimeException(e);
        }
    }

    public InputStream post(String data) throws RuntimeException {
        HttpPost post = new HttpPost(url.toString());
        addBodyRequest(post, data);
        try {
            return execute(post);
        } catch (IOException e) {
            Log.e(TAG, "Error on POST of " + url, e);
            throw new RuntimeException(e);
        }
    }

    public InputStream put(String id, String data) throws RuntimeException {
        HttpPut put = new HttpPut(appendIdToURL(id));
        addBodyRequest(put, data);
        try {
            return execute(put);
        } catch (IOException e) {
            Log.e(TAG, "Error on PUT of " + url, e);
            throw new RuntimeException(e);
        }
    }

    public InputStream delete(String id) throws RuntimeException {
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

    private InputStream execute(HttpRequestBase method) throws IOException {
        method.setHeader("Accept", "application/json");
        method.setHeader("Content-type", "application/json");
        return client.execute(method).getEntity().getContent();
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
