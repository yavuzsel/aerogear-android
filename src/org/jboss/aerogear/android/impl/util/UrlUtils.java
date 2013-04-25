/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.android.impl.util;

import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {

    private static final String TAG = UrlUtils.class.getSimpleName();
    
    private UrlUtils(){}
    
     /**
     * @param baseURL 
     * @param endpoint
     * @return a new url baseUrl + endpoint
     * @throws IllegalArgumentException if baseUrl+endpoint is not a real url.
     */
    public static URL appendToBaseURL(final URL baseURL, String endpoint) {
        try {
            String baseString = baseURL.toString();
            if (!baseString.endsWith("/")) {
                baseString += "/";
            }
            return new URL(baseURL.toString() + endpoint);
        } catch (MalformedURLException ex) {
            String message = "Could not append " + endpoint + " to "
                    + baseURL.toString();
            Log.e(TAG, message, ex);
            throw new IllegalArgumentException(message, ex);
        }
    }
    
}
