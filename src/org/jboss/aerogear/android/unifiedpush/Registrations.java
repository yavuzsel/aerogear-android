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
package org.jboss.aerogear.android.unifiedpush;

import java.util.HashMap;
import java.util.Map;
import org.jboss.aerogear.android.impl.unifiedpush.DefaultPushRegistrarFactory;

/**
 * This is the factory and accessors for PushRegistrars
 */
public class Registrations {
    
    private final PushRegistrarFactory factory;
    private final Map<String, PushRegistrar> registrars = new HashMap<String, PushRegistrar>();
    
    public Registrations() {
        this.factory = new DefaultPushRegistrarFactory();
    }

    public Registrations(PushRegistrarFactory factory) {
        this.factory = factory;
    }
    
    /**
     * @param name the name which will be used to look up the registrar later
     * @param config
     * @return 
     * 
     * @throws  IllegalArgumentException is config.type is not a supported type
     */
    public PushRegistrar push(String name, PushConfig config) {
        PushRegistrar registrar = factory.createPushRegistrar(config);
        registrars.put(name, registrar);
        return registrar;
    }
    
    public PushRegistrar get(String name) {
        return registrars.get(name);
    }
    
}
