package support;


import com.tagmycode.sdk.exception.TagMyCodeException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public abstract class TagMyCodeExceptionBaseTest extends BaseTest {

    @Test
    public abstract void exceptionIsSubclassOfTagMyCodeException();

    protected void assertClassIsSubclassOfTagMyCodeException(Class cls) {
        assertTrue(TagMyCodeException.class.isAssignableFrom(cls));
    }
}
