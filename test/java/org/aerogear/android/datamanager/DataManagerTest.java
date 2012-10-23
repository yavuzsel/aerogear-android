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

package org.aerogear.android.datamanager;

import org.aerogear.android.datamanager.DataManager;
import org.aerogear.android.datamanager.Store;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.aerogear.android.datamanager.StoreType.MEMORY;

public class DataManagerTest {

    private DataManager dataManager;

    @Before
    public void setup() {
        dataManager = new DataManager();
    }

    @Test
    public void testCreateStoreWithDefaulType() {
        Store store = dataManager.add("foo");

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    @Test
    public void testCreateStoreWithMemoryType() {
        Store store = dataManager.add("foo", MEMORY);

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }
    @Test
    public void testAddStoreWithDefaulType() {
        dataManager.add("foo");
        Store store = dataManager.get("foo");

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    @Test
    public void testAddStoreWithMemoryType() {
        dataManager.add("foo", MEMORY);
        Store store = dataManager.get("foo");

        assertNotNull("foo store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    @Test 
    public void testAndAddAndRemoveStores() {
        dataManager.add("foo", MEMORY);
        dataManager.add("bar");

        Store fooStore = dataManager.get("foo");
        assertNotNull("foo store could not be null", fooStore);
        
        Store barStore = dataManager.get("bar");
        assertNotNull("bar store could not be null", barStore);

        fooStore = dataManager.remove("foo");
        assertNotNull("foo store could not be null", fooStore);

        fooStore = dataManager.get("foo");
        assertNull("foo store should be null", fooStore);
    }

}