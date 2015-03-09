package support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ResourceReader {

    public String readFile(String filename) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s, file = "";
        while ((s = br.readLine()) != null) {
            file += s + "\n";
        }

        return file;
    }
}
