package com.tagmycode.sdk.exception;

import support.TagMyCodeExceptionBaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagMyCodeJsonExceptionTest extends TagMyCodeExceptionBaseTest {

    @Test
    @Override
    public void exceptionIsSubclassOfTagMyCodeException() {
        assertClassIsSubclassOfTagMyCodeException(TagMyCodeJsonException.class);
    }

    @Test
    public void defaultConstructor() {
        TagMyCodeJsonException exception = new TagMyCodeJsonException();
        assertEquals("Unable to parse JSON", exception.getMessage());
    }

    @Test
    public void constructorWithException() {
        TagMyCodeJsonException exception = new TagMyCodeJsonException(new Exception("A custom message"));
        assertEquals("A custom message", exception.getMessage());
    }
}
