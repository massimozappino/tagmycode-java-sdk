package support;


import org.junit.Test;


public abstract class ModelAbstractBaseTest extends BaseTest {

    @Test
    public abstract void newModelWithJsonObject() throws Exception;

    @Test
    public abstract void newModelWithJsonString() throws Exception;

    @Test
    public abstract void toJson() throws Exception;

    @Test
    public abstract void compareModelObject() throws Exception;

    @Test
    public abstract void serializeAndDeserialize() throws Exception;
}
