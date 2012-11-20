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
package org.aerogear.android.impl.datamanager;

import org.aerogear.android.datamanager.IdGenerator;
import org.aerogear.android.datamanager.Store;
import org.aerogear.android.datamanager.StoreType;
import org.aerogear.android.impl.core.PropertyNotFoundException;
import org.aerogear.android.impl.core.Scan;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Memory implementation of Store {@link Store}.
 */
public class MemoryStorage<T> implements Store<T> {

    private final Map<Serializable, T> data = new HashMap<Serializable, T>();
    private final IdGenerator idGenerator;

    public MemoryStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StoreType getType() {
        return StoreTypes.MEMORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> readAll() {
        return data.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T read(Serializable id) {
        return data.get(id);
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public void save(T item) {
        Serializable idValue;

        Field recordId = Scan.recordIdFieldIn(item.getClass());

        try {
            Method getMethod = item.getClass().getMethod("get" + capitalize(recordId.getName()));
            idValue = (Serializable) getMethod.invoke(item);

            if( idValue == null ) {
                idValue = idGenerator.generate();
                Method setMethod = item.getClass().getMethod("set" + capitalize(recordId.getName()), recordId.getType());
                setMethod.invoke(item, idValue);
            }
        } catch (Exception e) {
            throw new PropertyNotFoundException(item.getClass(), recordId.getName());
        }

        data.put(idValue, item);
    }

    private String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        data.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Serializable id) {
        data.remove(id);
    }
}
