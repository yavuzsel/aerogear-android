/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aerogear.android.authentication;

/**
 * A enum for the types of {@link  AuthenticationModule} {@link Authenticator}
 * knows how to build.
 */
public enum AuthType {
    REST("Rest");
   
    /**
     * In theory, one day, a developer will be able to add in her own
     * AuthTypes.  In theory, one day, DefaultAuthenticator will have a map 
     * of <String, Class> which will server as a lookup.  
     * 
     * IE this is future proofing.
     */
    private final String key;
    
    private AuthType(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
}
