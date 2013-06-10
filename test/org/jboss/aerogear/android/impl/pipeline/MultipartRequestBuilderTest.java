/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.android.impl.pipeline;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

@RunWith(RobolectricTestRunner.class)
public class MultipartRequestBuilderTest {

    private static final String STRING_DATA = "This is a String";
    
    @Test
    public void testGetContentType() {
        MultipartRequestBuilder<Object> builder = new MultipartRequestBuilder();
        assertTrue(builder.getContentType().startsWith("multipart/form-data;"));
    }
    
    @Test
    public void testBuilder() {
        
        final Queue<String> values = new ArrayDeque<String>(20);
        values.add("multipart/form-data; boundary=[\\w\\d-]+");
        
        values.add("--[\\w\\d-]+\r\n");
        values.add("Content-Disposition: form-data; name=\"string\"\r\n");
        values.add("Content-Type: text/plain; charset=US-ASCII\r\n");
        values.add("Content-Transfer-Encoding: 8bit\r\n");
        values.add("\r\n");
        values.add(STRING_DATA + "\r\n");
        values.add("--[\\w\\d-]+\r\n");
        values.add("Content-Disposition: form-data; name=\"files\"\r\n");
        values.add("Content-Type: multipart/mixed; boundary=[\\w\\d-]+\r\n");
        values.add("\r\n");
        values.add("--[\\w\\d-]+\r\n");
        values.add("Content-Disposition: file; filename=\"inputStream\"\r\n");
        values.add("Content-Type: application/octet-stream\r\n");
        values.add("Content-Transfer-Encoding: binary\r\n");
        values.add("\r\n");
        values.add("abcdef\r\n");
        values.add("--[\\w\\d-]+\r\n");
        values.add("Content-Disposition: file; filename=\"byteArray\"\r\n");
        values.add("Content-Type: application/octet-stream\r\n");
        values.add("Content-Transfer-Encoding: binary\r\n");
        values.add("\r\n");
        values.add("abcdef\r\n");
        values.add("--[\\w\\d-]+--\r\n");
        values.add("--[\\w\\d-]+--\r\n");
        MultiPartData data = new MultiPartData();
        MultipartRequestBuilder<MultiPartData> dataBuilder = new MultipartRequestBuilder<MultiPartData>();
        assertTrue(dataBuilder.getContentType().matches(values.poll()));
        byte[] out = dataBuilder.getBody(data);
        ByteArrayInputStream stream = new ByteArrayInputStream(out);
        StringBuilder stringBuilder = new StringBuilder();
        int readByte;
        while ((readByte = stream.read()) != -1) {
            stringBuilder.append((char)readByte);
            if (((char)readByte) == '\n') {
                assertTrue(stringBuilder.toString().matches(values.poll()));
                stringBuilder = new StringBuilder();
            }
        }
        
        
    }
    
    
    public static class MultiPartData{
        private byte[] byteArray = {'a','b','c','d','e','f'};
        private InputStream inputStream = new ByteArrayInputStream(byteArray);
        private String string = STRING_DATA;

        public byte[] getByteArray() {
            return byteArray;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public String getString() {
            return string;
        }

        public void setByteArray(byte[] byteArray) {
            this.byteArray = byteArray;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void setString(String string) {
            this.string = string;
        }
        
        
        
    } 
    
}
