package org.jboss.aerogear.android.impl.reflection;

import org.jboss.aerogear.android.impl.helper.Data;
import org.jboss.aerogear.android.impl.helper.DataWithNoPropertyId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class PropertyTest {

    @Test(expected = IllegalArgumentException.class)
    public void testUseNullClass() throws Exception {
        new Property(null, "id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUseNullFieldName() throws Exception {
        new Property(Data.class, null);
    }

    @Test(expected = FieldNotFoundException.class)
    public void testFieldNotFoundException() throws Exception {
        DataWithNoPropertyId data = new DataWithNoPropertyId();

        Property property = new Property(DataWithNoPropertyId.class, "monkey");
        property.setValue(data, 1);
    }

    @Test(expected = PropertyNotFoundException.class)
    public void testPropertyNotFoundException() throws Exception {
        DataWithNoPropertyId data = new DataWithNoPropertyId();

        Property property = new Property(DataWithNoPropertyId.class, "id");
        property.setValue(data, 1);
    }

    @Test
    public void testGetStringValue() throws Exception {
        Data data = new Data(1, "F50", "The best car in the world");

        Property property = new Property(Data.class, "name");
        Object value = property.getValue(data);

        assertEquals("F50", value);
    }

    @Test
    public void testSetStringValue() throws Exception {
        Data data = new Data(1, "F50", "The best car in the world");

        Property property = new Property(Data.class, "name");
        property.setValue(data, "Boxster");

        assertEquals("Boxster", data.getName());
    }

    @Test
    public void testGetBooleanValue() throws Exception {
        Data data = new Data(1, "F50", "The best car in the world", true);

        Property property = new Property(Data.class, "enable");
        Object value = property.getValue(data);

        assertEquals(true, value);
    }

    @Test
    public void testSetBooleanValue() throws Exception {
        Data data = new Data(1, "F50", "The best car in the world", true);

        Property property = new Property(Data.class, "enable");
        property.setValue(data, false);

        assertFalse(data.isEnable());
    }

}