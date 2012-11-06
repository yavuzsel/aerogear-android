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
import org.aerogear.android.datamanager.IdGenerator;
import org.aerogear.android.impl.datamanager.MemoryStorage;
import org.aerogear.android.datamanager.Store;
import org.aerogear.android.impl.datamanager.StoreType;
import org.aerogear.android.impl.core.HttpRestProvider;
import org.aerogear.android.impl.pipeline.PipeConfig;
import org.aerogear.android.impl.pipeline.RestAdapter;
import org.aerogear.android.impl.pipeline.Types;
import org.aerogear.android.pipeline.Pipe;

import java.net.MalformedURLException;
import java.net.URL;

import static org.aerogear.android.impl.datamanager.StoreType.MEMORY;

final class AdapterFactory {

    private AdapterFactory() {}

    public static Pipe createPipe(Class klass, PipeConfig config) {
        if (config.getType().equals(Types.REST)) {
            return new RestAdapter(klass, new HttpRestProvider(appendEndpoint(config.getUrl(), config.getEndpoint())));
        }
        throw new IllegalArgumentException("Type is not supported yet");
    }

    public static Store createStore(StoreType type, IdGenerator idGenerator) {
        if (type.equals(MEMORY)) {
            return new MemoryStorage(idGenerator);
        }
        throw new IllegalArgumentException("Type is not supported yet");
    }

    private static URL appendEndpoint(URL baseURL, String endpoint) {

        try {
            if( !baseURL.toString().endsWith("/")) {
                endpoint = "/" + endpoint;
            }
            return new URL(baseURL + endpoint + "/");
        } catch (MalformedURLException e) {
            Log.e("AeroGear", e.getMessage());
            return null;
        }
    }

}
