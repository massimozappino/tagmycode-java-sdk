package com.tagmycode.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class ResourceReader {

    public String readFile(String filename) throws IOException {
        String s, file = "";
        while ((s = getBufferedReader(filename).readLine()) != null) {
            file += s + "\n";
        }

        return file;
    }

    private BufferedReader getBufferedReader(String filename) {
        return new BufferedReader(new InputStreamReader(getInputStream(filename)));
    }

    public InputStream getInputStream(String filename) {
        // do not use Thread.currentThread().getContextClassLoader()
        // IntelliJ plugin does not read under the right directory
        return getClass().getResourceAsStream(filename);
    }

    public Properties getProperties(String filename) throws IOException {
        Properties properties = new Properties();
        properties.load(getInputStream(filename));
        return properties;
    }
}
