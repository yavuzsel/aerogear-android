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

import org.aerogear.android.query.Conditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple API wrapper to map a collection of typed objects to a backend URL
 *
 * @param <T> the data class in the collection
 */
public class Pipe<T> {
    private String url;
    private Class<T[]> exemplar;

    /**
     * Constructor
     *
     * @param url the base URL (i.e. "items" would map to "http://server/items/")
     * @param exemplar we seem to need this b/c we can't get T's Class object without it.
     */
    public Pipe(String url, Class<T[]> exemplar) {
        this.url = url;
        this.exemplar = exemplar;
    }

    /**
     * Add a new item to the collection.
     *
     * @param item new item to add
     * @throws Exception something bad happened.
     */
    public void add(T item) throws Exception {
        AeroGear.save(url, item);
    }

    /**
     * Remove an item from the backend.
     *
     * @param id the ID of the item to delete (gets tacked on the end of the collection URL)
     * @throws Exception
     */
    public void delete(String id) throws Exception {
        AeroGear.delete(url, id);
    }

    /**
     * Retrieve a typed List of all the items on the backend.
     *
     * @param existingList a List to reuse (gets cleared + refilled), or null to create a new one
     * @return the collection of deserialized T's from the backend
     * @throws Exception
     */
    public List<T> getAll(List<T> existingList) throws Exception {
        T[] items = AeroGear.get(url, exemplar);
        List<T> ret = existingList;
        if (ret == null) {
            ret = new ArrayList<T>(items.length);
        } else {
            ret.clear();
        }
        Collections.addAll(ret, items);
        return ret;
    }

    public List<T> getAll() throws Exception {
        return getAll(null);
    }

    /**
     * Query the backend for all items matching a given set of conditions.
     *
     * TODO: Discuss + implement?
     *
     * @param conditions placeholder for conditions (needs defining)
     * @return a List of matching T's from the back end
     * @throws Exception
     */
    public List<T> getMatching(Conditions conditions) throws Exception {
        return null;
    }
}
