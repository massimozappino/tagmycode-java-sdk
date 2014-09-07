package com.tagmycode.plugin.gui;

import com.tagmycode.plugin.AbstractTest;
import com.tagmycode.plugin.Framework;
import com.tagmycode.sdk.TagMyCode;
import com.tagmycode.sdk.model.ModelCollection;
import com.tagmycode.sdk.model.Snippet;
import org.junit.Before;
import org.junit.Test;
import support.FakeDocumentInsertText;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class SearchDialogTest extends AbstractTest {

    private Framework framework;

    @Before
    public void initFramework() {
        framework = createFramework();
    }

    @Test
    public void noResultsFound() throws Exception {
        mockClientSearchJavaGetsResults(framework);
        SearchSnippetDialog searchSnippetDialog = createSearchSnippetDialog();
        JList resultList = searchSnippetDialog.getResultList();

        makeASearchWithoutResults(searchSnippetDialog);

        assertSelectedFirstElementIs(resultList, null);
        assertResultSizeIs(resultList, 1);
        assertEquals("No results found", resultList.getModel().getElementAt(0));

        makeASearchWithResults(searchSnippetDialog);

        assertResultSizeIs(resultList, 2);
        assertSelectedFirstElementIs(resultList, resourceGenerate.aSnippetCollection().get(0));
    }

    @Test
    public void countResultsLabelPrintsCorrectText() throws Exception {
        mockClientSearchJavaGetsResults(framework);
        SearchSnippetDialog searchSnippetDialog = createSearchSnippetDialog();

        assertEquals("0 snippets found", searchSnippetDialog.getResultsFoundLabel().getText());

        makeASearchWithoutResults(searchSnippetDialog);
        assertEquals("0 snippets found", searchSnippetDialog.getResultsFoundLabel().getText());

        makeASearchWithResults(searchSnippetDialog);
        Thread.sleep(1000);

        assertEquals("2 snippets found", searchSnippetDialog.getResultsFoundLabel().getText());
    }

    @Test
    public void nullDocumentInsertDoNotShowInsertButton() throws Exception {
        SearchSnippetDialog searchSnippetDialog = new SearchSnippetDialog(null, framework, null);
        assertFalse(searchSnippetDialog.getInsertButton().isVisible());
    }

    @Test
    public void buttonsAreActiveOnlyWhenSnippetIsSelected() throws Exception {
        mockClientSearchJavaGetsResults(framework);
        SearchSnippetDialog searchSnippetDialog = createSearchSnippetDialog();

        assertInsertButtonIsDisabled(searchSnippetDialog);

        makeASearchWithResults(searchSnippetDialog);
        searchSnippetDialog.getResultList().setSelectedIndex(0);

        assertInsertButtonIsEnabled(searchSnippetDialog);

        makeASearchWithResults(searchSnippetDialog);
        assertInsertButtonIsDisabled(searchSnippetDialog);

        makeASearchWithoutResults(searchSnippetDialog);
        searchSnippetDialog.getResultList().setSelectedIndex(0);
        assertInsertButtonIsDisabled(searchSnippetDialog);
    }

    private void makeASearchWithoutResults(SearchSnippetDialog searchSnippetDialog) throws InterruptedException {
        makeASearch(searchSnippetDialog, "none");
    }

    private void makeASearchWithResults(SearchSnippetDialog searchSnippetDialog) throws InterruptedException {
        makeASearch(searchSnippetDialog, "java");
    }

    private void assertInsertButtonIsDisabled(SearchSnippetDialog searchSnippetDialog) {
        assertFalse(searchSnippetDialog.getInsertButton().isEnabled());
    }

    private void assertInsertButtonIsEnabled(SearchSnippetDialog searchSnippetDialog) {
        assertEquals(true, searchSnippetDialog.getInsertButton().isEnabled());
    }


    private SearchSnippetDialog createSearchSnippetDialog() {
        return new SearchSnippetDialog(new FakeDocumentInsertText(), framework, null);
    }

    private void assertResultSizeIs(JList resultList, int expected) {
        assertEquals(expected, resultList.getModel().getSize());
    }

    private void assertSelectedFirstElementIs(JList resultList, Object expected) {
        resultList.setSelectedIndex(0);
        assertEquals(expected, resultList.getSelectedValue());
    }

    private void makeASearch(SearchSnippetDialog searchSnippetDialog, String query) throws InterruptedException {
        searchSnippetDialog.getSearchTextBox().setText(query);
        searchSnippetDialog.getSearchButton().doClick();
        // TODO transform to wait for condition
        Thread.sleep(200);
    }

    protected void mockClientSearchJavaGetsResults(Framework framework) throws Exception {
        TagMyCode mockedTagMyCode = getMockedTagMyCode(framework);

        when(mockedTagMyCode.searchSnippets("java")).thenReturn(resourceGenerate.aSnippetCollection());
        when(mockedTagMyCode.searchSnippets("none")).thenReturn(new ModelCollection<Snippet>());
    }

}