package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.TagMyCodeApiProduction;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.model.LanguagesCollection;
import org.junit.Before;
import org.junit.Test;
import support.VoidOauthWallet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileNameToLanguageTest {
    private LanguagesCollection languages;

    @Before
    public void setupLanguages() throws TagMyCodeException {
        Client client = new Client(new TagMyCodeApiProduction(), "123", "456", new VoidOauthWallet());
        client.setOauthToken(new OauthToken("123", "456"));
        languages = new TagMyCode(client).fetchLanguages();
    }

    @Test
    public void mapExtensionsToLanguage() throws TagMyCodeException {
        assertExtensionHasCode("java", "java");
        assertExtensionHasCode("php", "php");
        assertExtensionHasCode("php", "phtml");
        assertExtensionHasCode("javascript", "js");
        assertExtensionHasCode("css", "css");
        assertExtensionHasCode("css", "scss");
        assertExtensionHasCode("html", "html");
        assertExtensionHasCode("xml", "xhtml");
        assertExtensionHasCode("xml", "xml");
        assertExtensionHasCode("bash", "sh");
        assertExtensionHasCode("bash", "bash");
        assertExtensionHasCode("ruby", "rb");
        assertExtensionHasCode("ruby", "erb");
        assertExtensionHasCode("sql", "sql");
        assertExtensionHasCode("c", "c");
        assertExtensionHasCode("cpp", "cpp");
        assertExtensionHasCode("python", "py");
    }

    protected void assertExtensionHasCode(String code, String extension) {
        assertEquals(code, FileNameToLanguage.getCodeByExtension(extension));
        assertNotNull(languages.findByCode(code));
    }

    @Test
    public void guessLanguageByFileExtensions() {
        assertCodeFromFileName(null, null);
        assertCodeFromFileName(null, "file");
        assertCodeFromFileName("java", "file.multiple.dots.java");
        assertCodeFromFileName("text", "file.txt");
        assertCodeFromFileName("java", "file.java");
        assertCodeFromFileName("java", "UPPERCASE.JAVA");
    }

    private void assertCodeFromFileName(String expected, String fileName) {
        assertEquals(expected, FileNameToLanguage.getCode(fileName));
    }
}
