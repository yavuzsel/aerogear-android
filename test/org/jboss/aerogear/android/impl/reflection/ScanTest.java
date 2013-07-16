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
package org.jboss.aerogear.android.impl.reflection;

import org.jboss.aerogear.android.RecordId;
import org.jboss.aerogear.android.impl.helper.Data;
import org.jboss.aerogear.android.impl.helper.DataWithNoIdConfigured;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScanTest {

    @Test(expected = RecordIdNotFoundException.class)
    public void testRecordIdNotFoundException() throws Exception {
        Scan.recordIdFieldNameIn(DataWithNoIdConfigured.class);
    }

    @Test
    public void testScanRecordId() throws Exception {
        String recordIdfieldName = Scan.recordIdFieldNameIn(Data.class);
        assertEquals("id", recordIdfieldName);
    }

    @Test
    public void testScanRecordIdInSuperClass() throws Exception {
        String recordIdfieldName = Scan.recordIdFieldNameIn(MyData.class);
        assertEquals("id", recordIdfieldName);
    }

    public static class SuperData {

        @RecordId
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

    public static class MyData extends SuperData {
    }

}
