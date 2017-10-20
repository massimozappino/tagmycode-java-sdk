package com.tagmycode.sdk.crash;

import com.tagmycode.sdk.Client;
import com.tagmycode.sdk.ParamList;
import com.tagmycode.sdk.exception.TagMyCodeException;
import org.scribe.model.Verb;

public class CrashClient {
    private Client client;

    public CrashClient(Client client) {

        this.client = client;
    }

    public void sendCrash(Crash crash) throws TagMyCodeException {
        ParamList paramList = new ParamList().addAll(crash.toMap());
        client.sendRequest("crash", Verb.POST, paramList);
    }
}
