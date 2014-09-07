package com.tagmycode.plugin;


import com.tagmycode.plugin.exception.TagMyCodeGuiException;
import com.tagmycode.plugin.gui.*;
import com.tagmycode.sdk.Client;
import com.tagmycode.sdk.TagMyCode;
import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.TagMyCodeApi;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.exception.TagMyCodeUnauthorizedException;
import com.tagmycode.sdk.model.LanguageCollection;
import com.tagmycode.sdk.model.Snippet;
import com.tagmycode.sdk.model.User;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class Framework {
    public static final int DAYS_BEFORE_RELOAD = 5;
    private final MainWindow mainWindow;
    private User account;
    private LanguageCollection languageCollection;
    private final Wallet wallet;
    private final Client client;
    private final TagMyCode tagMyCode;
    private final AbstractPreferences preferences;
    private final Frame mainFrame;
    private final IMessageManager messageManager;
    private final IConsole console;
    private final AbstractTaskFactory taskFactory;
    private SearchSnippetDialog searchSnippetDialog;

    public Framework(TagMyCodeApi tagMyCodeApi, FrameworkConfig frameworkConfig, String consumerKey, String consumerSecret) {
        wallet = new Wallet(frameworkConfig.getPasswordManager());
        client = new Client(tagMyCodeApi, consumerKey, consumerSecret);
        tagMyCode = new TagMyCode(client);
        this.preferences = frameworkConfig.getPreferences();
        this.messageManager = frameworkConfig.getMessageManager();
        this.mainFrame = frameworkConfig.getMainFrame();
        this.taskFactory = frameworkConfig.getTask();
        this.mainWindow = new MainWindow(this);
        this.console = mainWindow.getConsole();
        restoreData();
        getConsole().log("TagMyCode started");
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public void setLanguageCollection(LanguageCollection languageCollection) {
        this.languageCollection = languageCollection;
    }

    public User getAccount() {
        return account;
    }

    public LanguageCollection getLanguageCollection() {
        return languageCollection;
    }

    public void resetData() {
        account = null;
        languageCollection = null;
    }

    public AbstractTaskFactory getTaskFactory() {
        return taskFactory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Client getClient() {
        return client;
    }

    public IMessageManager getMessageManager() {
        return messageManager;
    }

    public OauthToken loadAccessTokenFormWallet() throws TagMyCodeGuiException {
        OauthToken oauthToken = wallet.loadOauthToken();
        client.setOauthToken(oauthToken);
        return oauthToken;
    }

    public AbstractPreferences getPreferences() {
        return preferences;
    }

    public void loadPreferences() throws TagMyCodeJsonException {
        account = preferences.getAccount();
        languageCollection = preferences.getLanguageCollection();
    }

    public void fetchAllData() throws TagMyCodeException {
        account = tagMyCode.getAccount();
        languageCollection = tagMyCode.getLanguages();
    }

    public void fetchAndStoreAllData() throws TagMyCodeException {
        fetchAllData();
        storeData();
    }

    public void storeData() throws TagMyCodeJsonException {
        try {
            preferences.setLanguageCollection(languageCollection);
            preferences.setAccount(account);
            preferences.setLastUpdate(new Date());
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    public void logout() {
        client.revokeAccess();
        try {
            wallet.deleteAccessToken();
        } catch (TagMyCodeGuiException e) {
            manageTagMyCodeExceptions(e);
        }
        preferences.clearAll();
    }

    public void refreshDataIfItIsOld() {
        try {
            fetchAndStoreAllData();
            console.log("Data refreshed");
        } catch (TagMyCodeException ignore) {
        }
    }

    public boolean isDataToBeRefreshed() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1 * DAYS_BEFORE_RELOAD);
        return preferences.getLastUpdate().before(cal.getTime());
    }

    public boolean isRefreshable() {
        return client.isAuthenticated() && isDataToBeRefreshed();
    }

    public boolean isInitialized() {
        return client.isAuthenticated() && account != null && languageCollection != null;
    }

    public void showAuthenticateDialog(ICallback... iCallback) {
        new AuthorizationDialog(this, iCallback, getMainFrame()).showAtCenter();
    }

    public void showSnippetDialog(Snippet snippet, String mimeType) {
        SnippetDialog snippetDialog = new SnippetDialog(this, mimeType, getMainFrame());
        snippetDialog.populateWithSnippet(snippet);
        snippetDialog.showAtCenter();
    }

    public void showSearchDialog(IDocumentInsertText documentUpdate) {
        if (searchSnippetDialog == null) {
            searchSnippetDialog = new SearchSnippetDialog(documentUpdate, this, getMainFrame());
        }
        new GuiThread().execute(new Runnable() {
            @Override
            public void run() {
                searchSnippetDialog.showAtCenter();
            }
        });
    }

    public Frame getMainFrame() {
        return mainFrame;
    }

    public void error(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                messageManager.error(message);
            }
        });
    }

    public void error() {
        error(new TagMyCodeException().getMessage());
    }

    public void manageTagMyCodeExceptions(TagMyCodeException exception) {
        if (exception instanceof TagMyCodeUnauthorizedException) {
            logoutAndAuthenticateAgain();
        } else {
            error();
        }
    }

    public void logoutAndAuthenticateAgain() {
        logout();
        showAuthenticateDialog();
    }

    public boolean canOperate() {
        if (!isInitialized()) {
            showAuthenticateDialog();
        }
        return isInitialized();
    }

    public IConsole getConsole() {
        return console;
    }

    public void restoreData() {
        try {
            loadAccessTokenFormWallet();
            loadPreferences();
        } catch (TagMyCodeJsonException e) {
            resetData();
        } catch (TagMyCodeGuiException e) {
            manageTagMyCodeExceptions(e);
        }
    }

    public void initialize(final ICallback[] callbacks) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    fetchAndStoreAllData();
                } catch (TagMyCodeException ex) {
                    manageTagMyCodeExceptions(ex);
                    logout();
                } finally {
                    if (callbacks != null) {
                        for (ICallback callback : callbacks) {
                            callback.doOperation();
                        }
                    }
                }
            }
        };

        try {
            getWallet().saveOauthToken(client.getOauthToken());
            getTaskFactory().create(runnable, "Initializing TagMyCode");
        } catch (TagMyCodeGuiException e) {
            manageTagMyCodeExceptions(e);
            logoutAndAuthenticateAgain();
        }


    }

    public TagMyCode getTagMyCode() {
        return tagMyCode;
    }
}
