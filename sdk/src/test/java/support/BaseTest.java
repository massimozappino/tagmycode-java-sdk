package support;

import org.junit.Before;
import org.scribe.model.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseTest {

    protected ResourceGenerate resourceGenerate;

    @Before
    public void configureResourceReader() {
        resourceGenerate = new ResourceGenerate();
    }

    public Response createResponseMock() {
        Response response = mock(Response.class);
        when(response.getBody()).thenReturn("{}");
        when(response.getCode()).thenReturn(200);
        when(response.isSuccessful()).thenReturn(true);
        when(response.getHeader("X-RateLimit-Limit")).thenReturn("100");
        when(response.getHeader("X-RateLimit-Remaining")).thenReturn("50");
        when(response.getHeader("X-RateLimit-Reset")).thenReturn("1387110772");

        return response;
    }
}
