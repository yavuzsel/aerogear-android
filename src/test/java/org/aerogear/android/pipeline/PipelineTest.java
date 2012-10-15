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

package org.aerogear.android.pipeline;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.aerogear.android.helper.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class PipelineTest {

    private URL url;

    @Before
    public void setup() throws MalformedURLException {
        url = new URL("http://server.com/context/");
    }

    @Test
    public void testAddNewPipeToPipeline() {
        Pipeline  pipeline = new Pipeline("tasks", Data.class, url);
        Pipe newPipe = pipeline.add("projects", Data.class, url);
        assertNotNull("Added pipe", newPipe);
    }

    @Test
    public void testAddNewPipeToPipelineWithType() {
        Pipeline  pipeline = new Pipeline("tasks", Data.class, url);
        Pipe newPipe = pipeline.add("projects", Data.class, url, Type.REST);
        assertNotNull("Added pipe", newPipe);
    }

    @Test
    public void testGetExistingPipe() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url);
        Pipe tasksPipe = pipeline.get("tasks");
        assertNotNull("received pipe", tasksPipe);
    }

    @Test
    public void testGetNonExistingPipe() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url);
        Pipe tasksPipe = pipeline.get("Footasks");
        assertNull("Not received pipe", tasksPipe);
    }

    @Test
    public void testRemoveExistingPipe() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url);

        Pipe tasksPipe = pipeline.remove("tasks");
        assertNotNull("deleted pipe", tasksPipe);

        tasksPipe = pipeline.get("tasks");
        assertNull("Not received pipe", tasksPipe);
    }

    @Test
    public void testRemoveNonExistingPipe() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url);

        Pipe fooPipe = pipeline.remove("foo");
        assertNull("Not deleted pipe", fooPipe);
    }

    @Test
    public void testPipeDefaultTypeProperty() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url);
        Pipe tasksPipe = pipeline.get("tasks");
        assertEquals("verifying the (default) type", Type.REST, tasksPipe.getType());
    }

    @Test
    public void testPipeURLProperty() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url);
        Pipe tasksPipe = pipeline.get("tasks");
        assertEquals("verifying the given URL", "http://server.com/context/tasks/", tasksPipe.getUrl().toString());
    }

    @Test
    public void testPipeTypeProperty() {
        Pipeline pipeline = new Pipeline("tasks", Data.class, url, Type.REST);
        Pipe tasksPipe = pipeline.get("tasks");
        assertEquals("verifying the (default) type", Type.REST, tasksPipe.getType());
    }

    @Test
    public void testEndpointURL() {
        Pipeline pipeline = new Pipeline("bad name", Data.class, url, "projects");
        Pipe myPipe = pipeline.get("bad name");
        assertEquals("verifying the given URL", "http://server.com/context/projects/", myPipe.getUrl().toString());
    }

    @Test
    public void testEndpointURLWithType() {
        Pipeline pipeline = new Pipeline("bad name", Data.class, url, "projects", Type.REST);
        Pipe myPipe = pipeline.get("bad name");
        assertEquals("verifying the given URL", "http://server.com/context/projects/", myPipe.getUrl().toString());
        assertEquals("verifying the type", Type.REST, myPipe.getType());
    }

    @Test
    public void testAddPipe() throws MalformedURLException {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        pipeline.add("foo", Data.class);

        Pipe newPipe = pipeline.get("foo");
        assertEquals("verifying the given URL", "http://server.com/context/foo/", newPipe.getUrl().toString());
        assertEquals("verifying the type", Type.REST, newPipe.getType());
    }

    @Test
    public void testAddPipeWithEndpoint() throws MalformedURLException {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        pipeline.add("bad name", Data.class, "foo");

        Pipe newPipe = pipeline.get("bad name");
        assertEquals("verifying the given URL", "http://server.com/context/foo/", newPipe.getUrl().toString());
    }

    @Test
    public void testAddPipeWithURLAndEndpoint() throws MalformedURLException {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        URL otherURL = new URL("http://server.com/otherContext/");
        pipeline.add("bad name", Data.class, otherURL, "foo");

        Pipe newPipe = pipeline.get("bad name");
        assertEquals("verifying the given URL", "http://server.com/otherContext/foo/", newPipe.getUrl().toString());
    }


    @Test
    public void testAddWithRestType() throws MalformedURLException {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        pipeline.add("foo", Data.class, Type.REST);

        Pipe newPipe = pipeline.get("foo");
        assertEquals("verifying the type", Type.REST, newPipe.getType());
    }

    @Test
    public void testAddWithEndpointAndRestType() throws MalformedURLException {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        pipeline.add("foo", Data.class, "bar", Type.REST);

        Pipe newPipe = pipeline.get("foo");
        assertEquals("verifying the type", Type.REST, newPipe.getType());
        assertEquals("verifying the given URL", "http://server.com/context/bar/", newPipe.getUrl().toString());
    }

    @Test
    public void testAddPipeWithoutEndpoint() throws MalformedURLException {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        URL otherURL = new URL("http://server.com/otherContext/");
        pipeline.add("foo", Data.class, otherURL);
        Pipe newPipe = pipeline.get("foo");
        assertEquals("verifying the given URL", "http://server.com/otherContext/foo/", newPipe.getUrl().toString());
    }

    @Test
    public void testAddPipeWithoutEndpointAndWithoutBaseURL() {
        Pipeline pipeline = new Pipeline("projects", Data.class, url);

        pipeline.add("foo", Data.class);
        Pipe newPipe = pipeline.get("foo");
        assertEquals("verifying the given URL", "http://server.com/context/foo/", newPipe.getUrl().toString());
    }

}
