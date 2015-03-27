package com.tagmycode.sdk.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultLanguageTest {
    @Test
    public void defaultValuesForTextLanguage() {
        DefaultLanguage language = new DefaultLanguage();
        assertEquals(1, language.getId());
        assertEquals("text", language.getCode());
        assertEquals("Text", language.getName());
    }
}