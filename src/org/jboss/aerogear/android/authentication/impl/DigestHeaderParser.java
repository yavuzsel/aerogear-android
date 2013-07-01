package org.jboss.aerogear.android.authentication.impl;

import java.util.HashMap;
import java.util.Map;

public final class DigestHeaderParser {

    
    private static enum States {

        DIGEST, KEY, VALUE
    };
    private static final String DIGEST = "Digest";
    private static final String WHITESPACE = "\\s";
    private static final String COMMA = ",";
    private static final String QUOTE = "\"";
    private static final String EQ = "=";
    

    public static Map<String, String> extractValues(String authenticateHeader) {
        States state = States.DIGEST;
        StringBuilder word = new StringBuilder();
        String key = "";
        String value = "";
        String valueTerminator = COMMA;
        
        Map<String, String> values = new HashMap<String, String>();
        authenticateHeader = authenticateHeader.trim();
        if (!authenticateHeader.startsWith(DIGEST)) {
            throw new IllegalArgumentException(authenticateHeader + " Did not begin with the Digest challenge string.");
        }

        for (Character character : authenticateHeader.toCharArray()) {
            if (matches(character, WHITESPACE)){
                continue;
            } 
            switch (state) {
                case DIGEST:
                    word.append(character);
                    if (word.lastIndexOf(DIGEST) != -1) {
                        word = new StringBuilder();
                        state = States.KEY;
                    }
                    break;
                case KEY:
                    if (matches(character, EQ)) {
                        key = word.toString();
                        word = new StringBuilder();
                        state = States.VALUE;
                        break;
                    } else if (matches(character, COMMA)){
                        break;
                    } else {
                        word.append(character);
                    }
                    break;
                case VALUE:
                    if (matches(character, valueTerminator)) {
                        value = word.toString();
                        word = new StringBuilder();
                        valueTerminator = COMMA;
                        values.put(key, value);
                        state = States.KEY;
                        break;
                    } else {
                        if (matches(character, QUOTE)) {
                            valueTerminator = QUOTE;
                            break;
                        } else {
                            word.append(character);
                        }
                    }
                    break;
            }
        }

        return values;

    }
    
    private static boolean matches(Character character, String regex) {
        String str = character.toString();
        return str.matches(regex);
    }

}
