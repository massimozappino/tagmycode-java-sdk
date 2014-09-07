package support;


import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.*;

import java.io.IOException;

public class ResourceGenerate {

    private JsonResourceReader resourceReader;

    public JsonResourceReader getResourceReader() {
        return resourceReader;
    }

    public ResourceGenerate() {
        resourceReader = new JsonResourceReader();
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

    public User anUser() throws IOException, TagMyCodeJsonException {
        return new User(resourceReader.readFile("user.json"));
    }

    public LanguageCollection aLanguageCollection() throws IOException, TagMyCodeJsonException {
        LanguageCollection languageCollection = new LanguageCollection();
        languageCollection.add(aLanguage());
        languageCollection.add(anotherLanguage());
        return languageCollection;
    }

    public ModelCollection<Snippet> aSnippetCollection() throws IOException, TagMyCodeJsonException {
        ModelCollection<Snippet> snippetCollection = new ModelCollection<Snippet>();
        snippetCollection.add(aSnippet());
        snippetCollection.add(anotherSnippet());
        return snippetCollection;
    }
}
