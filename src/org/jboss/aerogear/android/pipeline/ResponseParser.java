package org.jboss.aerogear.android.pipeline;

public interface ResponseParser<T> {

    public T handleResponse(String response, Class<T> responseType);
    
    public T[] handleArrayResponse(String response, Class<T[]> responseType);
    
}
