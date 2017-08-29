package integration;


import com.tagmycode.sdk.DateParser;
import com.tagmycode.sdk.SyncSnippets;
import com.tagmycode.sdk.TagMyCode;
import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.model.Snippet;
import com.tagmycode.sdk.model.SnippetsCollection;
import com.tagmycode.sdk.model.SnippetsDeletions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import support.ClientBaseTest;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SynchronizeSnippetsTest extends ClientBaseTest {
    private TagMyCode tagMyCode;

    @Before
    public void prepareData() throws TagMyCodeException {
        tagMyCode = spy(new TagMyCode(client));
        doNothing().when(tagMyCode).deleteSnippet(anyInt());
        doReturn(new Snippet()).when(tagMyCode).updateSnippet((Snippet) any());
    }

    @Test
    public void createSnippetFromServer() throws Exception {
        SnippetsCollection remoteSnippetsChanges = new SnippetsCollection();
        remoteSnippetsChanges.add(resourceGenerate.aSnippet());
        tagMyCodeSyncReturns(tagMyCode, remoteSnippetsChanges, new SnippetsDeletions());

        SnippetsCollection dirtySnippets = new SnippetsCollection();

        SyncSnippets syncSnippets = tagMyCode.syncSnippets(dirtySnippets, new SnippetsDeletions());

        assertEquals(resourceGenerate.aSnippet(), firstSnippetOf(syncSnippets));
    }

    @Test
    public void createSnippetFromClient() throws Exception {
        tagMyCodeSyncReturns(tagMyCode, new SnippetsCollection(), new SnippetsDeletions());

        SnippetsCollection dirtySnippets = new SnippetsCollection();
        Snippet localSnippet = resourceGenerate.aSnippet().setId(0).setTitle("local title").setLocalId(99);
        dirtySnippets.add(localSnippet);

        Date creationDate = new DateParser().parseDate("2017-11-22T13:11:25Z");
        doReturn(resourceGenerate.anotherSnippet()
                .setCreationDate(creationDate))
                .when(tagMyCode).createSnippet((Snippet) any());

        SyncSnippets syncSnippets = tagMyCode.syncSnippets(dirtySnippets, new SnippetsDeletions());

        verify(tagMyCode).createSnippet(localSnippet);

        assertEquals(creationDate.toString(), firstSnippetOf(syncSnippets).getCreationDate().toString());
        assertEquals(99, firstSnippetOf(syncSnippets).getLocalId());
    }

    @Test
    public void updateSnippetFromServer() throws Exception {
        SnippetsCollection remoteSnippetsChanges = new SnippetsCollection();
        remoteSnippetsChanges.add(resourceGenerate.aSnippet().setTitle("new title"));
        tagMyCodeSyncReturns(tagMyCode, remoteSnippetsChanges, new SnippetsDeletions());

        SnippetsCollection dirtySnippets = new SnippetsCollection();
        dirtySnippets.add(resourceGenerate.aSnippet());

        SyncSnippets syncSnippets = tagMyCode.syncSnippets(dirtySnippets, new SnippetsDeletions());

        assertEquals("new title", firstSnippetOf(syncSnippets).getTitle());
    }

    @Test
    public void updateSnippetFromClient() throws Exception {
        tagMyCodeSyncReturns(tagMyCode, new SnippetsCollection(), new SnippetsDeletions());

        Date updateDate = new DateParser().parseDate("2017-11-22T13:11:25Z");
        doReturn(resourceGenerate.aSnippet().setTitle("new title")
                .setUpdateDate(updateDate))
                .when(tagMyCode).updateSnippet((Snippet) any());

        SnippetsCollection dirtySnippets = new SnippetsCollection();
        dirtySnippets.add(resourceGenerate.aSnippet().setTitle("new title").setDirty(true));

        SyncSnippets syncSnippets = tagMyCode.syncSnippets(dirtySnippets, new SnippetsDeletions());

        verify(tagMyCode).updateSnippet(dirtySnippets.firstElement());
        assertEquals("new title", firstSnippetOf(syncSnippets).getTitle());
        assertEquals(updateDate.toString(), firstSnippetOf(syncSnippets).getUpdateDate().toString());
    }

    @Test
    public void deleteSnippetFromServer() throws Exception {
        SnippetsDeletions remoteDeletions = new SnippetsDeletions(12);
        tagMyCodeSyncReturns(tagMyCode, new SnippetsCollection(), remoteDeletions);

        SyncSnippets syncSnippets = tagMyCode.syncSnippets(new SnippetsCollection(), new SnippetsDeletions());

        assertEquals(1, syncSnippets.getDeletedSnippets().size());
        assertEquals(new Integer(12), syncSnippets.getDeletedSnippets().get(0));
    }

    @Test
    public void deleteSnippetFromClient() throws Exception {
        tagMyCodeSyncReturns(tagMyCode, new SnippetsCollection(), new SnippetsDeletions());

        SnippetsCollection dirtySnippets = new SnippetsCollection();
        dirtySnippets.add(resourceGenerate.aSnippet().setId(1));

        SnippetsDeletions localDeletions = new SnippetsDeletions(1);
        SyncSnippets syncSnippets = tagMyCode.syncSnippets(dirtySnippets, localDeletions);

        verify(tagMyCode, times(1)).deleteSnippet(1);
        assertEquals(0, syncSnippets.getDeletedSnippets().size());
    }

    @Test
    public void deleteSnippetsBeforeRequestChanges() throws TagMyCodeException, IOException {
        tagMyCodeSyncReturns(tagMyCode, new SnippetsCollection(), new SnippetsDeletions());

        tagMyCode.syncSnippets(new SnippetsCollection(), new SnippetsDeletions());

        InOrder inOrder = inOrder(tagMyCode);
        inOrder.verify(tagMyCode, times(1)).deleteSnippets((SnippetsDeletions) any());
        inOrder.verify(tagMyCode, times(1)).fetchSnippetsChanges(anyString());
    }

    @Test
    public void conflict() throws Exception {
        Snippet snippet = resourceGenerate.aSnippet().setId(1).setTitle("Title");
        tagMyCodeSyncReturns(tagMyCode, new SnippetsCollection(), new SnippetsDeletions());
        doThrow(new TagMyCodeApiException()).when(tagMyCode).updateSnippet((Snippet) any());
        doReturn(snippet).when(tagMyCode).createSnippet((Snippet) any());

        SnippetsCollection dirtySnippets = new SnippetsCollection();
        dirtySnippets.add(snippet);

        SyncSnippets syncSnippets = tagMyCode.syncSnippets(dirtySnippets, new SnippetsDeletions());

        verify(tagMyCode, times(1)).updateSnippet(dirtySnippets.firstElement());
        verify(tagMyCode, times(1)).createSnippet(dirtySnippets.firstElement());
        assertEquals("Title [Conflict]", firstSnippetOf(syncSnippets).getTitle());
    }

    private void tagMyCodeSyncReturns(TagMyCode tagMyCode, SnippetsCollection snippetsChanges, SnippetsDeletions remoteDeletions) throws TagMyCodeException {
        doReturn(snippetsChanges).when(tagMyCode).fetchSnippetsChanges(anyString());
        doReturn(remoteDeletions).when(tagMyCode).fetchDeletions(anyString());
    }

    private Snippet firstSnippetOf(SyncSnippets syncSnippets) {
        return syncSnippets.getChangedSnippets().firstElement();
    }
}
