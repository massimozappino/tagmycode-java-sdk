package com.tagmycode.sdk.exception;

import support.TagMyCodeExceptionBaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagMyCodeConnectionExceptionTest extends TagMyCodeExceptionBaseTest {

    @Test
    @Override
    public void exceptionIsSubclassOfTagMyCodeException() {
        assertClassIsSubclassOfTagMyCodeException(TagMyCodeConnectionException.class);
    }

    @Test
    public void defaultConstructor() {
        TagMyCodeConnectionException exception = new TagMyCodeConnectionException(new Exception("A custom message"));
        assertEquals("A custom message", exception.getMessage());
    }

}
