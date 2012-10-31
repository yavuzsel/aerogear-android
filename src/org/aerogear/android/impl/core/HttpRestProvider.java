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

package org.aerogear.android.impl.core;

import android.util.Log;
import org.aerogear.android.core.HttpProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.aerogear.android.core.HeaderAndBody;
import org.aerogear.android.core.HttpException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * 
 * 
 * 
 * These are tuned for Aerogear, assume the body is String data, and that
 * the headers don't do anything funny.
 * 

 */
public final class HttpRestProvider implements HttpProvider {

    private static final String TAG = "AeroGear";

    private final URL url;
    private final HttpClient client;

    private Map<String, String> defaultHeaders = new HashMap<String, String>();

    public HttpRestProvider(URL url) {
        this.url = url;
        this.client = new DefaultHttpClient();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public URL getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HeaderAndBody get() throws RuntimeException {
        try {
            return execute(new HttpGet(url.toString()));
        } catch (IOException e) {
            Log.e(TAG, "Error on GET of " + url, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HeaderAndBody post(String data) throws RuntimeException {

        HttpPost post = new HttpPost(url.toString());
        addBodyRequest(post, data);
        try {
            return execute(post);
        } catch (IOException e) {
            Log.e(TAG, "Error on POST of " + url, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HeaderAndBody put(String id, String data) throws RuntimeException {
        HttpPut put = new HttpPut(appendIdToURL(id));
        addBodyRequest(put, data);
        try {
            return execute(put);
        } catch (IOException e) {
            Log.e(TAG, "Error on PUT of " + url, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HeaderAndBody delete(String id) throws RuntimeException {
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

    private HeaderAndBody execute(HttpRequestBase method) throws IOException {
        method.setHeader("Accept", "application/json");
        method.setHeader("Content-type", "application/json");
        
        for (Entry<String, String> entry : defaultHeaders.entrySet() ) {
        	method.setHeader(entry.getKey(), entry.getValue());	
        }
        
        HttpResponse response = client.execute(method);
        
        int statusCode = response.getStatusLine().getStatusCode();
        byte[] data = EntityUtils.toByteArray(response.getEntity());
        response.getEntity().consumeContent();
        if (statusCode != 200) {
            throw new HttpException(data, statusCode);
        }
        
        Header[] headers = response.getAllHeaders();
        HeaderAndBody result = new HeaderAndBody(data, new HashMap<String, Object>(headers.length));
        
        for (Header header : headers) {
            result.setHeader(header.getName(), header.getValue());
        }
        
        return result;
    }

    private String appendIdToURL(String id) {
        StringBuilder newUrl = new StringBuilder(url.toString());
        if( !url.toString().endsWith("/")) {
            newUrl.append("/");
        }
        newUrl.append(id);
        return newUrl.toString();
    }


	@Override
	public void setDefaultHeader(String headerName, String headerValue) {
		defaultHeaders.put(headerName, headerValue);
	}

}
