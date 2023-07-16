package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String path;

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    private Map<String, String> cookies = new HashMap<>();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line = bufferedReader.readLine();
            if (line == null) {
                return;
            }

            readRequestLine(line);

            readHeader(bufferedReader);
            readCookie(headers.get("Cookie"));
            if (method.equals("POST")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                String body = IOUtils.readData(bufferedReader, contentLength);
                params = HttpRequestUtils.parseQueryString(body);
            }
        }
        catch (IOException io) {
            log.error(io.getMessage());
        }
    }

    private void readHeader(BufferedReader bufferIn) throws IOException {
        String line = bufferIn.readLine();

        while (line != null && !line.equals("")) {
            log.debug("header : {}", line);
            readHeaderInfo(line);
            line = bufferIn.readLine();
        }
    }

    private void readRequestLine(String requestLine) throws IOException {

        log.debug("request line : {}", requestLine);
        String[] tokens = requestLine.split(" ");
        method = tokens[0];

        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        }
        if (index != -1) {
            path = tokens[1].substring(0, index);
            params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
        }
    }

    private void readHeaderInfo(String line) {
        String[] keyValue = line.split(":");
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();

        headers.put(key, value);
    }

    private void readCookie(String cookies) {
        this.cookies = HttpRequestUtils.parseCookies(cookies);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
}
