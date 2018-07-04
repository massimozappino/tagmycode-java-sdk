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

    @Test
    public void getPath() throws IOException {
        SaveFilePath saveFilePath = new SaveFilePath("namespace");
        assertTrue(saveFilePath.getPath().contains(".tagmycode/namespace"));
    }

    @Test
    public void getPathWith() throws IOException {
        SaveFilePath saveFilePath = new SaveFilePath("namespace");
        assertTrue(saveFilePath.getPathWith("my_file").contains(".tagmycode/namespace/my_file"));
    }
}