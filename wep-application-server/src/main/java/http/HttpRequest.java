package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
    private String method;
    private String url;
    private String path;

    private Map<String, String> header = new HashMap<>();
    private Map<String, String> parameter = new HashMap<>();

    private Map<String, String> cookie = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        readHeader(bufferedReader);
        readParam(bufferedReader);
    }

    private void readHeader(BufferedReader bufferIn) throws IOException {
        String line = bufferIn.readLine();
        readRequestLine(line);

        while (!"".equals(line) && line != null) {
            line = bufferIn.readLine();
            readHeaderInfo(line);
        }
        readCookie(header.get("Cookie"));
    }

    private void readRequestLine(String line) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
    }

    private void readHeaderInfo(String line) {
        if ("".equals(line) || line == null) {
            return;
        }
        String[] keyValue = line.split(":");
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();

        header.put(key, value);
    }

    private void readCookie(String cookies) {
        cookie = HttpRequestUtils.parseCookies(cookies);
    }

    private void readParam(BufferedReader bufferIn) throws IOException {
        if (method.equals("GET")) {
            String[] tokens = url.split("\\?");
            path = tokens[0];
            String quaryString = tokens[1];
            parameter = HttpRequestUtils.parseQueryString(quaryString);
        }

        if (method.equals("POST")) {
            path = url;
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            String quaryString = IOUtils.readData(bufferIn, contentLength);
            parameter = HttpRequestUtils.parseQueryString(quaryString);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getParameter(String key) {
        return parameter.get(key);
    }

    public String getCookie(String key) {
        return cookie.get(key);
    }
}
