package support;


import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.*;

import java.io.IOException;

public class ResourceGenerate {

    private ResourceReader resourceReader;

    public ResourceGenerate() {
        resourceReader = new ResourceReader();
    }

    public ResourceReader getResourceReader() {
        return resourceReader;
    }

    public Snippet aSnippet() throws IOException, TagMyCodeJsonException {
        return new Snippet(resourceReader.readFile("snippet.json"));
    }

    public Snippet anotherSnippet() throws IOException, TagMyCodeJsonException {
        return new Snippet(resourceReader.readFile("snippet2.json"));
    }

    public Language aLanguage() throws IOException, TagMyCodeJsonException {
        return new Language(resourceReader.readFile("language.json"));
    }

    public Language anotherLanguage() throws IOException, TagMyCodeJsonException {
        return new Language(resourceReader.readFile("language2.json"));
    }

    public User aUser() throws IOException, TagMyCodeJsonException {
        return new User(resourceReader.readFile("user.json"));
    }

    public LanguagesCollection aLanguageCollection() throws IOException, TagMyCodeJsonException {
        LanguagesCollection languageCollection = new LanguagesCollection();
        languageCollection.add(aLanguage());
        languageCollection.add(anotherLanguage());
        return languageCollection;
    }

    public SnippetsCollection aSnippetCollection() throws IOException, TagMyCodeJsonException {
        SnippetsCollection snippetCollection = new SnippetsCollection();
        snippetCollection.add(aSnippet());
        snippetCollection.add(anotherSnippet());
        return snippetCollection;
    }

    public String aSnippetsLastUpdate() {
        return "Sun, 24 Jan 2016 20:00:00 GMT";
    }
}
