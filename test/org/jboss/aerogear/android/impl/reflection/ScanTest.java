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

    public static class MyData extends SuperData {}

}
