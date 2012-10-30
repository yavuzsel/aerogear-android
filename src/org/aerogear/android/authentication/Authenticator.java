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

import org.aerogear.android.Builder;

/**
 * This is a factory and manager for {@link AuthenticationModule}
 * 
 *  It allows you to add and remove authentication modules.
 */
public interface Authenticator {
    
    /**
     * 
     * Creates a new {@link AuthenticationModule } 
     * 
     * @param name a key to use to lookup the Module later
     * @param moduleBuilder a configured module to build and add to the 
     *        Authenticator
     * @return 
     */
    public AuthenticationModule add(String name, Builder<? extends AuthenticationModule> moduleBuilder);
   

    /**
     * Looks up and returns the AuthenticationModule if it exists.
     * 
     * If name isn't valid, return null.
     * 
     * @param name name of AuthorizationModule object
     * @return 
     */
    public AuthenticationModule get(String name);
    
     /**
     * Looks up and removes the AuthenticationModule if it exists.
     * 
     * @param name name of AuthorizationModule object
     * @return the AuthenticationModule which was removed; null if one was not found
     */
    public AuthenticationModule remove(String name);
    
    
}
