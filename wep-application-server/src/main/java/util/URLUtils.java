package util;

public class URLUtils {

    public static String getURL(String requestLine) {
        if (requestLine == null) {
            return "/";
        }
        String[] info = requestLine.split(" ");
        return info[1];
    }
}
