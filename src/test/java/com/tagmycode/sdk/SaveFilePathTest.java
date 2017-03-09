package com.tagmycode.sdk;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;


public class SaveFilePathTest {

    @Test
    public void getAndCreatePathDoesNotThrowException() throws IOException {
        String namespace = "test_save_file_path";
        SaveFilePath saveFilePath = new SaveFilePath(namespace);
        String path = saveFilePath.getPath();
        assertTrue(path.contains(namespace));
        new File(path).delete();
    }
}