package http;

import java.io.BufferedReader;
import java.io.IOException;
import util.URLUtils;

public class RequestHeader {
    private String url;
    private String httpMethod;
    private int contentLength = 0;

    private String cookies;

    public RequestHeader(BufferedReader inputStream) throws IOException {
        readHeader(inputStream);
    }

    private void readHeader(BufferedReader bufferIn) throws IOException {
        String line = bufferIn.readLine();
        this.httpMethod = URLUtils.getHTTPMethod(line);
        this.url = URLUtils.getURL(line);

        while (!"".equals(line) && line != null) {
            line = bufferIn.readLine();
            readContentLength(line);
            readCookies(line);
        }
    }

    private void readContentLength(String line) {
        if (line.contains("Content-Length")) {
            contentLength = Integer.parseInt(line.split(" ")[1]);
        }
    }

    private void readCookies(String line) {
        if (line.contains("Cookie")) {
            cookies = line.split(": ")[1];
        }
    }

    public String getUrl() {
        return url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getCookies() {
        return cookies;
    }
}
