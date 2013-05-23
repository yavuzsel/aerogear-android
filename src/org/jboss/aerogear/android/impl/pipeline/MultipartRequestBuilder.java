/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jboss.aerogear.android.impl.pipeline;

import android.util.Log;
import android.webkit.MimeTypeMap;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jboss.aerogear.android.pipeline.RequestBuilder;
import org.jboss.aerogear.android.pipeline.TypeAndStream;

/**
 * This class generates a Multipart request with the type multipart/form-data
 *
 * It will load the entire contents of files into memory before it uploads them.
 *
 */
public class MultipartRequestBuilder<T> implements RequestBuilder<T> {

    private static final String TAG = MultipartRequestBuilder.class.getSimpleName();
    private static final String lineEnd = "\r\n";
    private static final String twoHyphens = "--";
    private final String boundary = UUID.randomUUID().toString();
    private final String CONTENT_TYPE = "multipart/form-data; boundary=" + boundary;
    private final String OCTECT_STREAM_MIME_TYPE = "application/octet-stream";

    @Override
    public byte[] getBody(T data) {


        ByteArrayOutputStream binaryStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(binaryStream);

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass());
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

            Map<String, String> fields = new HashMap<String, String>(descriptors.length);
            Map<String, TypeAndStream> files = new HashMap<String, TypeAndStream>(descriptors.length);

            for (PropertyDescriptor propertyDescriptor
                    : descriptors) {

                Object value = propertyDescriptor.getReadMethod().invoke(data);
                if (value.getClass().isPrimitive()) {
                    fields.put(propertyDescriptor.getName(), value.toString());

                } else {
                    if (value instanceof byte[]) {
                        files.put(propertyDescriptor.getName(),
                                new TypeAndStream(OCTECT_STREAM_MIME_TYPE,
                                propertyDescriptor.getName(),
                                new ByteArrayInputStream((byte[]) value)));
                    } else if (value instanceof InputStream) {
                        files.put(propertyDescriptor.getName(),
                                new TypeAndStream(OCTECT_STREAM_MIME_TYPE,
                                propertyDescriptor.getName(),
                                (InputStream) value));
                    } else if (value instanceof File) {
                        files.put(propertyDescriptor.getName(),
                                new TypeAndStream(getMimeType((File) value),
                                ((File) value).getName(),
                                new FileInputStream((File) value)));
                    } else if (value instanceof TypeAndStream) {
                        files.put(propertyDescriptor.getName(),
                                (TypeAndStream) value);
                    } else {
                        throw new IllegalArgumentException(propertyDescriptor.getName() + " is not a supported type for Multipart uplaod");
                    }
                }
            }


            for (Map.Entry<String, String> field : fields.entrySet()) {
                setField(dataOutputStream, field.getKey(), field.getValue());
            }

            
            if (files.size() == 1) {
                Map.Entry<String, TypeAndStream> pair = files.entrySet().iterator().next();
                TypeAndStream type = pair.getValue();
                String name = pair.getKey();
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\""+name+"\"; filename=\""+type.getFileName()+"\"" + lineEnd);
                dataOutputStream.writeBytes("Content-Type: "+ type.getMimeType() + lineEnd);
                dataOutputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                int b;
                while ((b = type.getInputStream().read()) != -1) {
                    dataOutputStream.write(b);
                }
            } else if (files.size() > 1) {
                String newBoundary = UUID.randomUUID().toString();
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"files\"" + lineEnd);
                dataOutputStream.writeBytes("Content-Type: multipart/mixed; boundary=" + newBoundary + lineEnd);


                for (Map.Entry<String, TypeAndStream> file : files.entrySet()) {
                    TypeAndStream type = file.getValue();
                    dataOutputStream.writeBytes(twoHyphens + newBoundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: file; filename=\"" + type.getFileName() + "\"" + lineEnd);
                    dataOutputStream.writeBytes("Content-Type: "+ type.getMimeType() + lineEnd);
                    dataOutputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    int b;
                    while ((b = type.getInputStream().read()) != -1) {
                        dataOutputStream.write(b);
                    }
                }
            }
            return binaryStream.toByteArray();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    private void setField(DataOutputStream dataOutputStream, String name, Object value) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
        dataOutputStream.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(value.toString() + lineEnd);
    }

    private String getMimeType(File file) throws MalformedURLException {
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.toURI().toURL().toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}
