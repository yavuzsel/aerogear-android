/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.core;

import java.util.Arrays;

/**
 *
 * If an HTTP Request does not return status code 200 then this will 
 * be thrown.
 * 
 * @author summers
 */
public class HttpException extends RuntimeException {
   
    /**
     * The body of the http response.
     */
    private byte[] data;
    
    /**
     * The returned status code
     */
    private int statusCode;

    public HttpException(byte[] data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public HttpException(byte[] data, int statusCode, String message) {
        super(message);
        this.data = data;
        this.statusCode = statusCode;
    }

    public HttpException(byte[] data, int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.data = data;
        this.statusCode = statusCode;
    }

    public HttpException(byte[] data, int statusCode, Throwable cause) {
        super(cause);
        this.data = data;
        this.statusCode = statusCode;
    }

    
    
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    public int getStatusCode() {
        return statusCode;
    }

}
