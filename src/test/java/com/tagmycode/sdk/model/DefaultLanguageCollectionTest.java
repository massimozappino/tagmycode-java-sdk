package com.tagmycode.sdk.model;


import org.junit.Test;
import support.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultLanguageCollectionTest extends BaseTest {

    @Test
    public void testConstructor() throws Exception {
        DefaultLanguageCollection defaultLanguageCollection = new DefaultLanguageCollection();
        assertTrue(defaultLanguageCollection instanceof LanguagesCollection);
        assertEquals(1, defaultLanguageCollection.size());
        assertEquals(new DefaultLanguage(), defaultLanguageCollection.get(0));
    }
}
