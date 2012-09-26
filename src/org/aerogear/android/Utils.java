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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Put specific network code behind a few utility APIs for the moment.
 *
 * TODO: Build this right, DRY it up, etc.
 */
public class Utils {
    public static final String TAG = "AeroGear";

    HttpClient client = new DefaultHttpClient();

    public Utils() {
    }

    public InputStream get(String url) {
        HttpGet get = new HttpGet(getServerURL(url));

        // TODO: Figure out appropriate headers, authentication, etc.
        get.addHeader("X-AeroGear-Client", AeroGear.apiKey);

        return getResponseStream(url, get);
    }

    public InputStream post(String url, String data) {
        HttpPost post = new HttpPost(getServerURL(url));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentType("application/json");
        post.setEntity(entity);

        return getResponseStream(url, post);
    }

    public InputStream put(String url, String data) {
        HttpPut put = new HttpPut(getServerURL(url));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentType("application/json");
        put.setEntity(entity);

        return getResponseStream(url, put);
    }

    private InputStream getResponseStream(String url, HttpRequestBase post) {
        try {
            final HttpResponse response = client.execute(post);
            return response.getEntity().getContent();
        } catch (IOException e) {
            // TODO: Real error handling
            Log.e(TAG, "Error on " + post.getMethod() + " of " + getServerURL(url), e);
            return null;
        }
    }

    public String getServerURL(String url) {
        return AeroGear.rootUrl + "/" + url;
    }

    public void delete(String url) {
        HttpDelete delete = new HttpDelete(getServerURL(url));
        try {
            client.execute(delete);
        } catch (IOException e) {
            // TODO: Real error handling
            Log.e(TAG, "Error on DELETE of " + getServerURL(url), e);
        }
    }
}
