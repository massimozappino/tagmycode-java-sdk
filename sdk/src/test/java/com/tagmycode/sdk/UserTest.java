package com.tagmycode.sdk;

import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.User;
import support.ModelAbstractBaseTest;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UserTest extends ModelAbstractBaseTest {

    @Test
    public void newUser() {
        User u = new User();
        assertEquals(0, u.getId());
    }

    @Override
    @Test
    public void newModelWithJsonObject() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("id", 1)
                .put("username", "my.username")
                .put("firstname", "Name")
                .put("lastname", "Surname")
                .put("email", "myfake@email.not");
        User u = new User(jsonObject);
        assertUserValues(u, 1);
    }

    @Override
    @Test
    public void newModelWithJsonString() throws IOException, TagMyCodeJsonException {
        String jsonString = resourceGenerate.getResourceReader().readFile("user.json");
        User u = new User(jsonString);
        assertUserValues(u, 1);
    }

    @Override
    @Test
    public void toJson() throws Exception {
        User user = new User();
        user.setId(1)
                .setUsername("my.username")
                .setFirstname("Name")
                .setLastname("Surname")
                .setEmail("myfake@email.not");
        assertTrue(user.equals(resourceGenerate.anUser()));
    }

    @Override
    @Test
    public void compareModelObject() throws Exception {
        User model1 = resourceGenerate.anUser();
        User model2 = resourceGenerate.anUser();
        assertTrue(model1.equals(model2));

        model2.setUsername("false");
        assertFalse(model1.equals(model2));
    }

    @Override
    @Test
    public void serializeAndDeserialize() throws Exception {
        User model = resourceGenerate.anUser();
        User restored = new User(model.toJson());
        assertTrue(model.equals(restored));
    }

    private void assertUserValues(User u, int id) {
        assertEquals(id, u.getId());
        assertEquals("my.username", u.getUsername());
        assertEquals("Name", u.getFirstname());
        assertEquals("Surname", u.getLastname());
        assertEquals("myfake@email.not", u.getEmail());
    }
}
