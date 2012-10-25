/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
