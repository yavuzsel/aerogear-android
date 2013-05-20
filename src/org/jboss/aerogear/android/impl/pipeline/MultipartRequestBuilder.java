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

import org.jboss.aerogear.android.pipeline.RequestBuilder;

/**
 * This class generates a Multipart request with the type multipart/form-data
 * 
 * It will load the entire contents of files into memory before it uploads 
 * them.  
 * 
 */
public class MultipartRequestBuilder<T> implements RequestBuilder<T>{

    public static final String CONTENT_TYPE = "multipart/form-data";
    
    @Override
    public byte[] getBody(T data) {
        for (int i = 0; i < 100; i++) {
            
        }
        
        return new byte[]{};
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }
    
    
    
}
