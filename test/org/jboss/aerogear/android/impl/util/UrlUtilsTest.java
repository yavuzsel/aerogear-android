package org.jboss.aerogear.android.impl.util;

import org.junit.Test;

import java.net.URL;

import static org.jboss.aerogear.android.impl.util.UrlUtils.appendToBaseURL;
import static org.junit.Assert.assertEquals;

public class UrlUtilsTest {

    @Test
    public void testBothHaveSlash() throws Exception {
        URL baseURL = new URL("http://fakeurl.com/");
        String endpoint = "/endpoint";
        URL expectURL = new URL("http://fakeurl.com/endpoint");

        assertEquals(expectURL, appendToBaseURL(baseURL, endpoint));
    }

    @Test
    public void testOnlyBaseURLHasSlash() throws Exception {
        URL baseURL = new URL("http://fakeurl.com/");
        String endpoint = "endpoint";
        URL expectURL = new URL("http://fakeurl.com/endpoint");

        assertEquals(expectURL, appendToBaseURL(baseURL, endpoint));
    }

    @Test
    public void testOnlyEndpointHasSlash() throws Exception {
        URL baseURL = new URL("http://fakeurl.com");
        String endpoint = "/endpoint";
        URL expectURL = new URL("http://fakeurl.com/endpoint");

        assertEquals(expectURL, appendToBaseURL(baseURL, endpoint));
    }

    @Test
    public void testBothWithoutSlash() throws Exception {
        URL baseURL = new URL("http://fakeurl.com");
        String endpoint = "endpoint";
        URL expectURL = new URL("http://fakeurl.com/endpoint");

        assertEquals(expectURL, appendToBaseURL(baseURL, endpoint));
    }

}
