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

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.aerogear.android.Pipeline;
import org.aerogear.android.pipeline.Pipe;
import org.aerogear.android.impl.helper.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.*;
import static org.aerogear.android.impl.pipeline.Type.*;

@RunWith(RobolectricTestRunner.class)
public class PipelineTest {

    private URL url;

    @Before
    public void setup() throws MalformedURLException {
        url = new URL("http://server.com/context/");
    }

    @Test
    public void testAddPipe() throws MalformedURLException {
        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class);

        assertEquals("verifying the given URL", "http://server.com/context/data/", newPipe.getUrl().toString());
        assertEquals("verifying the type", REST, newPipe.getType());
    }

    @Test
    public void testAddPipeWithEndpoint() throws MalformedURLException {
        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class, new PipeConfig("bad name", url, "foo"));

        assertEquals("verifying the given URL", "http://server.com/context/foo/", newPipe.getUrl().toString());
    }

    @Test
    public void testAddPipeWithType() throws MalformedURLException {
        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class, new PipeConfig("foo", url, REST));

        assertEquals("verifying the type", REST, newPipe.getType());
    }

    @Test
    public void testAddPipeWithUrl() throws MalformedURLException {
        URL otherURL = new URL("http://server.com/otherContext/");

        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class, new PipeConfig("foo", otherURL));

        assertEquals("verifying the given URL", "http://server.com/otherContext/foo/", newPipe.getUrl().toString());
    }


    @Test
    public void testAddPipeWithEndpointAndType() throws MalformedURLException {
        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class, new PipeConfig("foo", url, "bar", REST));

        assertEquals("verifying the type", REST, newPipe.getType());
        assertEquals("verifying the given URL", "http://server.com/context/bar/", newPipe.getUrl().toString());
    }

    @Test
    public void testAddPipeWithEndpointAndURL() throws MalformedURLException {
        URL otherURL = new URL("http://server.com/otherContext/");

        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class, new PipeConfig("bad name", otherURL, "foo"));

        assertEquals("verifying the given URL", "http://server.com/otherContext/foo/", newPipe.getUrl().toString());
    }

    @Test
    public void testAddPipeWithTypeAndUrl() throws MalformedURLException {
        URL otherURL = new URL("http://server.com/otherContext/");

        Pipeline pipeline = new Pipeline(url);
        Pipe newPipe = pipeline.pipe(Data.class, new PipeConfig("foo", otherURL, REST));

        assertEquals("verifying the type", REST, newPipe.getType());
        assertEquals("verifying the given URL", "http://server.com/otherContext/foo/", newPipe.getUrl().toString());
    }

    @Test
    public void testGetExistingPipe() {
        Pipeline pipeline = new Pipeline(url);
        pipeline.pipe(Data.class, new PipeConfig("foo", url));

        Pipe fooPipe = pipeline.get("foo");
        assertNotNull("received pipe", fooPipe);
    }

    @Test
    public void testGetNonExistingPipe() {
        Pipeline pipeline = new Pipeline(url);

        Pipe fooPipe = pipeline.get("Footasks");
        assertNull("Not received pipe", fooPipe);
    }

    @Test
    public void testRemoveExistingPipe() {
        Pipeline pipeline = new Pipeline(url);
        pipeline.pipe(Data.class, new PipeConfig("foo", url));

        Pipe fooPipe = pipeline.remove("foo");
        assertNotNull("deleted pipe", fooPipe);

        fooPipe = pipeline.get("foo");
        assertNull("Not received pipe", fooPipe);
    }

    @Test
    public void testRemoveNonExistingPipe() {
        Pipeline pipeline = new Pipeline(url);

        Pipe fooPipe = pipeline.remove("foo");
        assertNull("Not deleted pipe", fooPipe);
    }

}
