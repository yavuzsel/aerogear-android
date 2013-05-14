/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.jboss.aerogear.android.impl.pipeline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.aerogear.android.pipeline.ResponseParser;

public class GsonResponseParser<T> implements ResponseParser<T>{

    private final Gson gson;

    public GsonResponseParser() {
        this.gson = new Gson();
    }

    public GsonResponseParser(GsonBuilder gsonBuilder) {
        this.gson = gsonBuilder.create();
    }
    
    @Override
    public T handleResponse(String response, Class<T> responseType) {
        return gson.fromJson(response, responseType);
    }

    @Override
    public T[] handleArrayResponse(String response, Class<T[]> responseType) {
        return gson.fromJson(response, responseType);
    }
    
}
