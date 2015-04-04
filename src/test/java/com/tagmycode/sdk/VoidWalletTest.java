package com.tagmycode.sdk;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class VoidWalletTest {

    @Test
    public void testSaveOauthToken() throws Exception {
        VoidWallet voidWallet = new VoidWallet();
        assertTrue(voidWallet.saveOauthToken(null));
    }
}