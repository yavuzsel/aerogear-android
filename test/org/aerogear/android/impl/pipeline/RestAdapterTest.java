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

package org.aerogear.android.impl.pipeline;

import com.google.gson.GsonBuilder;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import junit.framework.Assert;
import org.aerogear.android.pipeline.Pipe;
import org.aerogear.android.impl.core.HttpStubProvider;
import org.aerogear.android.impl.helper.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import org.aerogear.android.Callback;

@RunWith(RobolectricTestRunner.class)
public class RestAdapterTest {

    private URL url;

    @Before
    public void setup() throws MalformedURLException {
        url = new URL("http://server.com/context/");
    }

    @Test
    public void testPipeTypeProperty() {
        Pipe restPipe = new RestAdapter(Data.class, new HttpStubProvider(url));
        Assert.assertEquals("verifying the (default) type", Types.REST, restPipe.getType());
    }

    @Test
    public void testPipeURLProperty() {
        Pipe restPipe = new RestAdapter(Data.class, new HttpStubProvider(url));
        assertEquals("verifying the given URL", "http://server.com/context/", restPipe.getUrl().toString());
    }
    
    @Test 
    public void testGsonBuilderProperty() throws ParseException, InterruptedException {
        GsonBuilder builderDMY = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        GsonBuilder builderYMD = new GsonBuilder().setDateFormat("yyyy/MM/dd");
        
        final StringBuilder request = new StringBuilder("");
        
        HttpStubProvider provider = new HttpStubProvider(url){

                             @Override
                             public byte[] put(String id, String data) {
                                 request.delete(0, request.length());
                                 request.append(data);
                                 return data.getBytes();
                             }

                             @Override
                             public byte[] post(String data) {
                                 request.delete(0, request.length());
                                 request.append(data);
                                 return data.getBytes();
                             }
                             
                             
                         };
        
        Pipe<DateId> restPipeDMY = new RestAdapter<DateId>(DateId.class, provider, builderDMY);
        final CountDownLatch latch = new CountDownLatch(1);
        final DateId dateId = new DateId();
        dateId.date = new SimpleDateFormat("MM/dd/yyyy").parse("12/18/1983");
 
        restPipeDMY.save(dateId, new Callback<DateId> () {

            @Override
            public void onSuccess(DateId data) {
                assertEquals("{\"date\":\"18/12/1983\"}", request.toString());
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                throw new RuntimeException(e);
            }
        });
        
        
        latch.await(2, TimeUnit.SECONDS);
        
    }
    
    public final static class DateId {
        Date date;
        
        public String getId() {
            return "1";
        }

        @Override
        public boolean equals(Object obj) {
            try {
                return date.equals(((DateId)obj).date);
            } catch (Throwable ignore)        {
                return false;
            }
        }
        
        
        
    }

}