package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {

    private Map<String, HttpSession> sessions = new HashMap<>();

    public void addSession(String uuid, Object value) {
        sessions.put(uuid, new HttpSession());
    }

    public HttpSession getSession(String uuid) {
        return sessions.get(uuid);
    }
}
