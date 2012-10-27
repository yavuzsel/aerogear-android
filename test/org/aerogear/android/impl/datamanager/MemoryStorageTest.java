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

import org.aerogear.android.impl.helper.Data;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryStorageTest {

    private MemoryStorage<Data> store;

    @Before
    public void setup() {
        store = new MemoryStorage<Data>();
    }

    @Test
    public void testStoreType() {
        assertEquals("verifying the type", StoreType.MEMORY, store.getType());
    }

    @Test
    public void testSave() {
        store.save(new Data(1L, "foo", "desc of foo"));
    }

    @Test()
    public void testRead() {
        store.save(new Data(1L, "foo", "desc of foo"));
        Data data = store.read("1");
        assertNotNull("data could not be null", data);
    }

    @Test
    public void testRemove() {
        store.save(new Data(1L, "foo", "desc of foo"));
        store.save(new Data(2L, "bar", "desc of bar"));

        Data foo = store.read("1");
        assertNotNull("foo could not be null", foo);

        Data bar = store.read("2");
        assertNotNull("bar could not be null", bar);

        store.remove(bar);

        foo = store.read("1");
        assertNotNull("foo could not be null", foo);

        bar = store.read("2");
        assertNull("bar should be null", bar);
    }

    @Test
    public void testReset() {
        store.save(new Data(1L, "foo", "desc of foo"));
        store.save(new Data(2L, "bar", "desc of bar"));

        Data foo = store.read("1");
        assertNotNull("foo could not be null", foo);

        Data bar = store.read("2");
        assertNotNull("bar could not be null", bar);

        store.reset();

        foo = store.read("1");
        assertNull("foo should be null", foo);

        bar = store.read("2");
        assertNull("bar should be null", bar);
    }

}
