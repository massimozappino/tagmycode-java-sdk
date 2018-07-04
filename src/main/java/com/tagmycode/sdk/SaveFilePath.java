package com.tagmycode.sdk;

import java.io.File;
import java.io.IOException;

public class SaveFilePath {

    private final String path;

    public SaveFilePath(String namespace) throws IOException {
        String basePath = getBasePath();
        createDirectories(basePath);
        path = basePath + File.separator + namespace;
    }

    public String getPath() {
        return path;
    }

    public String getPathWith(String filename) {
        return path + File.separator + filename;
    }

    private void createDirectories(String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Directory " + directory.getAbsolutePath() + "was not created");
            }
        }
    }

    private String getBasePath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + ".tagmycode";
    }
}
