/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android.authentication;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aerogear.android.authentication.impl.DefaultAuthenticator;
import org.aerogear.android.authentication.impl.RestAuthenticationModule;
import org.aerogear.android.impl.pipeline.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
/**
 *
 * @author summers
 */
@RunWith(RobolectricTestRunner.class)
public class AuthenticatorTest {
    
    private static final URL SIMPLE_URL;
    private static String SIMPLE_MODULE_NAME  = "simple";
    
    static {
        try {
            SIMPLE_URL = new URL("http", "localhost", 80, "/");
        } catch (MalformedURLException ex) {
            Logger.getLogger(AuthenticatorTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    @Test
    public void testAddSimpleAuthenticator() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        AuthenticationModule simpleAuthModule = authenticator.add(SIMPLE_MODULE_NAME, new RestAuthenticationModule.Builder());
        
        assertNotNull(simpleAuthModule);
        
    }
    
    @Test
    public void testAddAndGetSimpleAuthenticator() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        AuthenticationModule simpleAuthModule = authenticator.add(SIMPLE_MODULE_NAME, new RestAuthenticationModule.Builder());
        assertEquals(simpleAuthModule, authenticator.get(SIMPLE_MODULE_NAME));
    }
    
    @Test
    public void testGetNullAuthModule() {
        DefaultAuthenticator authenticator = new DefaultAuthenticator();
        assertNull(authenticator.get(SIMPLE_MODULE_NAME));
    }
}
