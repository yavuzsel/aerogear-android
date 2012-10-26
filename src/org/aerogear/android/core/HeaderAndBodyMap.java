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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a convenience to wrap up headers from a 
 * HTTPResponse with its entity.
 */
public class HeaderAndBodyMap extends HashMap<String, String> {
    
    private final byte[] body;

    public HeaderAndBodyMap(byte[] body, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.body= Arrays.copyOf(body, body.length);
    }

    public HeaderAndBodyMap(byte[] body, int initialCapacity) {
        super(initialCapacity);
        this.body= Arrays.copyOf(body, body.length);
    }

    public HeaderAndBodyMap(byte[] body) {
        this.body= Arrays.copyOf(body, body.length);
    }

    public HeaderAndBodyMap(byte[] body, Map<? extends String, ? extends String> m) {
        super(m);
        this.body= Arrays.copyOf(body, body.length);
    }
    
    public byte[] getBody() {
        return Arrays.copyOf(body, body.length);    
    }
    
}
