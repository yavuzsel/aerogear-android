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
 *
 */
public interface AddAuthBuilder<T extends AuthenticationModule> extends Builder<T> {
  
    
    /**
     * 
     * Overrides the value for enrollEndpoint
     * 
     * @param enrollEndpoint
     * @return 
     */
    AddAuthBuilder<T> enrollEndpoint(String enrollEndpoint);

    /**
     * 
     * Overrides the value for loginEndpoint
     * 
     * @param loginEndpoint
     * @return 
     */
    AddAuthBuilder<T> loginEndpoint(String loginEndpoint);

    /**
     * Overrides the value for enrollEndpoint
     * 
     * @param logoutEndpoint
     * @return 
     */
    AddAuthBuilder<T> logoutEndpoint(String logoutEndpoint);
    
    
    /**
     * Will build and add a value to a Authenticator
     * 
     * @param name A key to use to fetch the module using get
     * @return 
     */
    public T add(String name) ;

    
}
