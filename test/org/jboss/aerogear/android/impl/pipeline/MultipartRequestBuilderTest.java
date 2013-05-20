
package org.jboss.aerogear.android.impl.pipeline;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MultipartRequestBuilderTest {

    @Test
    public void testGetContentType() {
        MultipartRequestBuilder<Object> builder = new MultipartRequestBuilder();
        assertEquals("multipart/form-data", builder.getContentType());
    }
    
}
