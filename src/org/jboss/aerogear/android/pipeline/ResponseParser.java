/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.android.pipeline;

public interface ResponseParser<T> {

    public T handleResponse(String response, Class<T> responseType);
    
    public T[] handleArrayResponse(String response, Class<T[]> responseType);
    
}
