package org.jboss.aerogear.android.impl.reflection;

import org.jboss.aerogear.android.impl.helper.Data;
import org.jboss.aerogear.android.impl.helper.DataWithNoIdConfigured;
import org.jboss.aerogear.android.impl.helper.MyData;
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

}
