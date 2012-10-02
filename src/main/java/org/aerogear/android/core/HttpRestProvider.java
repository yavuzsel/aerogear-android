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

    public String get() {
        return execute(new HttpGet(url.toString()));
    }

    public void post(String data) {
        HttpPost post = new HttpPost(url.toString());
        addBodyRequest(post, data);
        execute(post);
    }

    public void put(String id, String data) {
        HttpPut put = new HttpPut(appendId(id));
        addBodyRequest(put, data);
        execute(put);
    }

    public void delete(String id) {
        HttpDelete delete = new HttpDelete(appendId(id));
        try {
            client.execute(delete);
        } catch (IOException e) {
            // TODO: Real error handling
            Log.e(TAG, "Error on DELETE of " + url, e);
        }
    }

    private void addBodyRequest(HttpEntityEnclosingRequestBase requestBase, String data) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        requestBase.setEntity(entity);
    }

    private String execute(HttpRequestBase method) {
        try {
            method.setHeader("Accept", "application/json");
            method.setHeader("Content-type", "application/json");
            return EntityUtils.toString(client.execute(method).getEntity());
        } catch (IOException e) {
            // TODO: Real error handling
            Log.e(TAG, "Error on " + method.getMethod() + " of " + method.getURI().toString(), e);
            return null;
        }
    }

    private String appendId(String id) {
        StringBuilder newUrl = new StringBuilder(url.toString());
        if( !url.toString().endsWith("/")) {
            newUrl.append("/");
        }
        newUrl.append(id);
        return newUrl.toString();
    }

}
