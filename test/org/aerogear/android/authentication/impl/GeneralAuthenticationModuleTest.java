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
package org.aerogear.android.authentication.impl;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.aerogear.android.Callback;
import org.aerogear.android.Provider;
import org.aerogear.android.authentication.AuthenticationModule;
import org.aerogear.android.authentication.AuthorizationFields;
import org.aerogear.android.core.HeaderAndBody;
import org.aerogear.android.core.HttpProvider;
import org.aerogear.android.impl.core.HttpProviderFactory;
import org.aerogear.android.impl.helper.Data;
import org.aerogear.android.impl.helper.TestUtil;
import org.aerogear.android.impl.pipeline.RestAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class GeneralAuthenticationModuleTest implements AuthenticationModuleTest {

    private static final URL SIMPLE_URL;

    static {
        try {
            SIMPLE_URL = new URL("http://localhost:8080/todo-server");
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Test
    public void applySecurityTokenOnURL() throws Exception {

        HttpProviderFactory factory = mock(HttpProviderFactory.class);
        when(factory.get(anyObject())).thenReturn(mock(HttpProvider.class));

        AuthorizationFields authFields = new AuthorizationFields();
        authFields.addQueryParameter("token", TOKEN);

        AuthenticationModule urlModule = mock(AuthenticationModule.class);
        when(urlModule.isLoggedIn()).thenReturn(true);
        when(urlModule.onSecurityApplicationRequested()).thenReturn(authFields);

        RestAdapter<Data> adapter = new RestAdapter<Data>(Data.class, SIMPLE_URL);
        TestUtil.setPrivateField(adapter, "httpProviderFactory", factory);
        adapter.setAuthenticationModule(urlModule);

        adapter.read(new Callback<List<Data>>() {

            @Override
            public void onSuccess(List<Data> data) {
            }

            @Override
            public void onFailure(Exception e) {
            }
        });

        verify(factory).get(new URL(SIMPLE_URL.toString() + "?token=" + TOKEN));
    }

}
