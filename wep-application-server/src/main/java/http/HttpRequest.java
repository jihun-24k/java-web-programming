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

    private RequestLine requestLine;

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line = bufferedReader.readLine();
            if (line == null) {
                return;
            }

            requestLine = new RequestLine(line);

            readHeader(bufferedReader);
            if (getMethod().isPost()) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                String body = IOUtils.readData(bufferedReader, contentLength);
                params = HttpRequestUtils.parseQueryString(body);
            }
            else {
                params = requestLine.getParams();
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

    private void readHeaderInfo(String line) {
        String[] keyValue = line.split(":");
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();

        headers.put(key, value);
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public HttpCookie getCookies() {
        return new HttpCookie(getHeader("Cookie"));
    }

    public HttpSession getSession() {
        return HttpSessions.getSession(getCookies().getCookie("JSESSIONID"));
    }
}
