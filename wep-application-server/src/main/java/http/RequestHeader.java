package http;

import java.io.BufferedReader;
import java.io.IOException;
import util.URLUtils;

public class RequestHeader {
    private String url;
    private String httpMethod;
    private int contentLength;

    public RequestHeader(BufferedReader inputStream) throws IOException {
        readHeader(inputStream);
    }

    private void readHeader(BufferedReader bufferIn) throws IOException {
        String line = bufferIn.readLine();
        this.httpMethod = URLUtils.getHTTPMethod(line);
        this.url = URLUtils.getURL(line);

        while (!"".equals(line) && line != null) {
            line = bufferIn.readLine();
        }
    }
}
