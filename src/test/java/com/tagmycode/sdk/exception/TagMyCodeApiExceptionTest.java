package com.tagmycode.sdk.exception;

import com.tagmycode.sdk.ClientResponse;
import support.TagMyCodeExceptionBaseTest;
import org.junit.Test;
import org.scribe.model.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagMyCodeApiExceptionTest extends TagMyCodeExceptionBaseTest {

    @Test
    public void exceptionIsSubclassOfTagMyCodeException() {
        assertClassIsSubclassOfTagMyCodeException(TagMyCodeApiException.class);
    }

    @Test
    public void constructorWithClientResponseReturnValidStatusCode() {
        TagMyCodeApiException exception = new TagMyCodeApiException(new ClientResponse(responseMockWithError()));
        assertEquals(400, exception.getHttpStatusCode());
        assertEquals("My custom message error", exception.getMessage());
    }

    private Response responseMockWithError() {
        Response response = mock(Response.class);
        when(response.getBody()).thenReturn("{\"code\":10,\"message\":\"My custom message error\",\"description\":\"No description\"}");
        when(response.getCode()).thenReturn(400);
        when(response.isSuccessful()).thenReturn(false);

        return response;
    }

}
