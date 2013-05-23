
package org.jboss.aerogear.android.impl.pipeline;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import org.jboss.aerogear.android.impl.helper.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MultipartRequestBuilderTest {

    @Test
    public void testGetContentType() {
        MultipartRequestBuilder<Object> builder = new MultipartRequestBuilder();
        assertTrue(builder.getContentType().startsWith("multipart/form-data;"));
    }
    
    @Test
    public void testMultipartSetsMultiPart() throws Exception {
        
        Data d = new Data("nname", "desc");
        
         for (PropertyDescriptor propertyDescriptor
                    : Introspector.getBeanInfo(Data.class, Object.class).getPropertyDescriptors()) {

                System.out.println(propertyDescriptor.getName() + propertyDescriptor.getReadMethod().invoke(d).toString());
            }
        
        throw new IllegalStateException("Not yet implemented");
    }
    
    @Test
    public void testMultipartUpload() {
        throw new IllegalStateException("Not yet implemented");
    }
    
}
