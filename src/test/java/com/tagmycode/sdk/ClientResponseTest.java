package com.tagmycode.sdk;

import org.junit.Before;
import org.junit.Test;
import support.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientResponseTest extends BaseTest {

    private ClientResponse clientResponse;

    @Before
    public void initClientResponse() {
        clientResponse = new ClientResponse(createResponseMock());
    }

    @Test
    public void bodyIsCorrect() {
        assertEquals("{}", clientResponse.getBody());
    }

    @Test
    public void hasRateLimit() {
        assertNotNull(clientResponse.getRateLimit());
    }

    @Test
    public void hasCorrectHttpStatusCode() {
        assertEquals(200, clientResponse.getHttpStatusCode());
    }

    @Test
    public void isError() {
        assertEquals(false, clientResponse.isError());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals("Sun, 24 Jan 2016 20:00:00 GMT", clientResponse.getLastUpdate());
    }
}
