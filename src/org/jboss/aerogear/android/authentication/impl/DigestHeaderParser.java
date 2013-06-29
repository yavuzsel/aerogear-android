package org.jboss.aerogear.android.authentication.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DigestHeaderParser {

    private static final String DIGEST = "Digest";
    private static final String DELIMITERS = "[\\s,]";
    
    public static Map<String, String> extractValues(String authenticateHeader) {
        Map<String, String> values = new HashMap<String, String>();
        List<String> tokenList = Arrays.asList(authenticateHeader.split( DELIMITERS));
        tokenList = Lists.newArrayList(Collections2.filter(tokenList, new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return !Strings.isNullOrEmpty(input);
            }
        }));
        
        if (!tokenList.remove(0).equals(DIGEST)) {
            throw new IllegalArgumentException(authenticateHeader + " Did not begin with the Digest challenge string.");
        }
        
        for (String token : tokenList) {
            String[] pair = token.split("=", 2);
            String key = pair[0];
            String value = pair[1];
            value = value.replaceAll("^\"", "").replaceAll("\"$", "");
            values.put(key, value);
        }
        
        return values;
        
    }
    
}
