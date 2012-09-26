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

import org.junit.Test;

import static org.mockito.Mockito.*;

public class AddAndUpdateTest {

    public static class Data {
        private Integer id;
        private String text;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * Make sure that we deal correctly with @see AeroGear#save for objects with/without IDs.
     *
     * @throws Exception
     */
    @Test
    public void testAddThenUpdate() throws Exception {
        Utils utils = mock(Utils.class);
        AeroGear.setUtils(utils);

        when(utils.put(anyString(), anyString())).thenReturn(null);
        when(utils.post(anyString(), anyString())).thenReturn(null);

        Data thingWithID = new Data();
        thingWithID.setText("Exciting data value");

        // First, save a blank one (which should POST)
        AeroGear.save("url", thingWithID);

        verify(utils).post(anyString(), anyString());
        verify(utils, never()).put(anyString(), anyString());

        // Now save one with an ID (which should PUT)
        thingWithID.setId(23);
        AeroGear.save("url", thingWithID);

        verify(utils).put(anyString(), anyString());
        verify(utils, times(1)).post(anyString(), anyString());
    }
}
