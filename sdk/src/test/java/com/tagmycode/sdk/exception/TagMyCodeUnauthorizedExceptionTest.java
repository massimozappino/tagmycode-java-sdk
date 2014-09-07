package com.tagmycode.sdk.exception;

import support.TagMyCodeExceptionBaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagMyCodeUnauthorizedExceptionTest extends TagMyCodeExceptionBaseTest {
    @Test
    @Override
    public void exceptionIsSubclassOfTagMyCodeException() {
        assertClassIsSubclassOfTagMyCodeException(TagMyCodeUnauthorizedException.class);
    }

    @Test
    public void defaultConstructor() {
        TagMyCodeUnauthorizedException exception = new TagMyCodeUnauthorizedException();
        assertEquals("Unauthorized", exception.getMessage());
    }
}
