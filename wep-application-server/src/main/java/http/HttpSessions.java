package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {

    private static Map<String, HttpSession> sessions = new HashMap<>();

    public static HttpSession getSession(String uuid) {

        HttpSession session = sessions.get(uuid);

        if (session == null) {
            session = new HttpSession(uuid);
            sessions.put(uuid, session);
        }
        return session;
    }

    public static void remove(String uuid) {
        sessions.remove(uuid);
    }
}
