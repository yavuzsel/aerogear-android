package org.aerogear.android.impl.datamanager;

import org.aerogear.android.core.TypeDescriptor;
import org.aerogear.android.datamanager.IdGenerator;
import org.aerogear.android.datamanager.Store;
import org.aerogear.android.datamanager.StoreFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class StubStoreFactory implements StoreFactory {
    @Override
    public Store createStore(TypeDescriptor type, IdGenerator idGenerator) {
        return new Store() {
            @Override
            public TypeDescriptor getType() {
                return new TypeDescriptor() {
                    @Override
                    public String getName() {
                        return "Stub";
                    }
                };
            }

            @Override
            public Collection readAll() {
                return new ArrayList();
            }

            @Override
            public Object read(Serializable id) {
                return new Object();
            }

            @Override
            public void save(Object item) {
            }

            @Override
            public void reset() {
            }

            @Override
            public void remove(Serializable id) {
            }
        };
    }
}
