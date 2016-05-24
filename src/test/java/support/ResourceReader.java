package support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ResourceReader {

    public String readFile(String filename) throws IOException {
        String s, file = "";
        BufferedReader bufferedReader = getBufferedReader(filename);
        while ((s = bufferedReader.readLine()) != null) {
            file += s + "\n";
        }

        return file;
    }

    private BufferedReader getBufferedReader(String filename) {
        return new BufferedReader(new InputStreamReader(getInputStream(filename)));
    }

    private InputStream getInputStream(String filename) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    }
}
