package org.jboss.aerogear.android.impl.helper;

/**
 * User: schullto
 * Date: 5/17/13
 * Time: 9:17 PM
 */
public class ExtendsData extends Data{
    public ExtendsData(String name, String description) {
        super(name, description);
    }

    public ExtendsData(Integer id, String name, String description) {
        super(id, name, description);
    }

    public ExtendsData(Integer id, String name, String description, boolean enable) {
        super(id, name, description, enable);
    }
}
