/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication;

/**
 *
 * @author summers
 */
public enum AuthType {
    REST("Rest");
    
    private final String key;
    
    private AuthType(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
}
